package com.idtech.nuosucivilaid.controller;

import com.idtech.nuosucivilaid.entity.CivilArticle;
import com.idtech.nuosucivilaid.repository.CivilArticleRepository;
import com.idtech.nuosucivilaid.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/civil/article")
public class CivilArticleController {

    @Autowired
    private CivilArticleRepository articleRepository;

    @GetMapping("/search")
    public Result<List<CivilArticle>> search(@RequestParam String keyword) {
        log.info("法条搜索, 关键词: {}", keyword);
        return Result.success(articleRepository.searchArticles(keyword));
    }

    // 【新增】按分类获取法条
    @GetMapping("/category/{id}")
    public Result<List<CivilArticle>> getByCategory(@PathVariable Long id) {
        log.info("获取分类法条, CategoryID: {}", id);
        return Result.success(articleRepository.findByCategoryIdAndIsDeletedFalseOrderByArticleNumAsc(id));
    }
}