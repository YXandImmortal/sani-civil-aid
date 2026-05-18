package com.idtech.sanicivilaid.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.github.jelmerk.hnswlib.core.DistanceFunctions;
import com.github.jelmerk.hnswlib.core.Item;
import com.github.jelmerk.hnswlib.core.SearchResult;
import com.github.jelmerk.hnswlib.core.hnsw.HnswIndex;
import com.idtech.sanicivilaid.entity.BizCivilFaq;
import com.idtech.sanicivilaid.repository.BizCivilFaqRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class FaqVectorIndexService {

    @Autowired
    private BizCivilFaqRepository faqRepository;

    @Autowired
    private EmbeddingService embeddingService;

    @Value("${deepseek.embedding.dimensions:1536}")
    private int dimensions;

    @Value("${deepseek.embedding.similarity-threshold:0.78}")
    private float similarityThreshold;

    private HnswIndex<String, float[], FaqVectorItem, Float> index;
    private Map<String, BizCivilFaq> faqMap;

    @PostConstruct
    public void init() {
        try {
            List<BizCivilFaq> faqs = faqRepository.findAll();
            int validCount = 0;
            for (BizCivilFaq faq : faqs) {
                if (!Boolean.TRUE.equals(faq.getIsDeleted())) {
                    validCount++;
                }
            }

            this.index = HnswIndex
                    .newBuilder(dimensions, DistanceFunctions.FLOAT_COSINE_DISTANCE, Math.max(validCount * 2, 100))
                    .withM(16)
                    .withEfConstruction(200)
                    .withEf(50)
                    .build();

            this.faqMap = new ConcurrentHashMap<>();

            int successCount = 0;
            for (BizCivilFaq faq : faqs) {
                if (Boolean.TRUE.equals(faq.getIsDeleted())) {
                    continue;
                }

                float[] vector = resolveEmbedding(faq);
                if (vector != null) {
                    faqMap.put(faq.getId(), faq);
                    index.add(new FaqVectorItem(faq.getId(), vector));
                    successCount++;
                }
            }

            log.info("FAQ 向量索引构建完成，成功加载 {} / {} 条", successCount, faqs.size());
        } catch (Exception e) {
            log.error("FAQ 向量索引初始化失败", e);
            this.index = null;
            this.faqMap = new ConcurrentHashMap<>();
        }
    }

    private float[] resolveEmbedding(BizCivilFaq faq) {
        String cached = faq.getQuestionEmbedding();
        if (cached != null && !cached.isBlank()) {
            try {
                return parseJsonArray(cached);
            } catch (Exception e) {
                log.warn("FAQ [{}] 的缓存 embedding 解析失败，将重新生成", faq.getId());
            }
        }

        try {
            float[] vector = embeddingService.embed(faq.getQuestionCn());
            faq.setQuestionEmbedding(toJsonArray(vector));
            faqRepository.save(faq);
            return vector;
        } catch (Exception e) {
            log.error("FAQ [{}] embedding 生成失败: {}", faq.getId(), e.getMessage());
            return null;
        }
    }

    public FaqSearchResult search(String question) {
        if (index == null || faqMap.isEmpty()) {
            return null;
        }

        try {
            float[] vector = embeddingService.embed(question);
            List<SearchResult<FaqVectorItem, Float>> results = index.findNearest(vector, 1);

            if (results == null || results.isEmpty()) {
                return null;
            }

            SearchResult<FaqVectorItem, Float> top = results.getFirst();
            float distance = top.distance();
            float similarity = 1.0f - distance;

            if (similarity >= similarityThreshold) {
                BizCivilFaq faq = faqMap.get(top.item().id());
                if (faq != null) {
                    log.debug("向量检索命中 FAQ，相似度: {}", similarity);
                    return new FaqSearchResult(faq, similarity);
                }
            }

            return null;
        } catch (Exception e) {
            log.error("向量检索失败", e);
            return null;
        }
    }

    private float[] parseJsonArray(String json) {
        JSONArray array = JSONUtil.parseArray(json);
        float[] result = new float[array.size()];
        for (int i = 0; i < array.size(); i++) {
            result[i] = array.getFloat(i);
        }
        return result;
    }

    private String toJsonArray(float[] vector) {
        JSONArray array = new JSONArray();
        for (float v : vector) {
            array.add(v);
        }
        return array.toString();
    }

    @Getter
    public static class FaqSearchResult {
        private final BizCivilFaq faq;
        private final float similarity;

        public FaqSearchResult(BizCivilFaq faq, float similarity) {
            this.faq = faq;
            this.similarity = similarity;
        }
    }

    private static class FaqVectorItem implements Item<String, float[]> {
        private final String id;
        private final float[] vector;

        FaqVectorItem(String id, float[] vector) {
            this.id = id;
            this.vector = vector;
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public float[] vector() {
            return vector;
        }

        @Override
        public int dimensions() {
            return vector.length;
        }

        @Override
        public long version() {
            return 0L;
        }
    }
}
