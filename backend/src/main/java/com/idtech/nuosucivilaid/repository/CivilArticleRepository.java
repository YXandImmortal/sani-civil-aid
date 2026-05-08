package com.idtech.nuosucivilaid.repository;

import com.idtech.nuosucivilaid.entity.CivilArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CivilArticleRepository extends JpaRepository<CivilArticle, Long> {

    // 模糊搜索：支持汉/彝关键词
    @Query("SELECT a FROM CivilArticle a WHERE " +
            "(a.contentCn LIKE %:keyword% OR a.contentNuosu LIKE %:keyword% OR CAST(a.articleNum AS string) = :keyword) " +
            "AND a.isDeleted = false")
    List<CivilArticle> searchArticles(@Param("keyword") String keyword);

    // 【新增】按分类查询
    List<CivilArticle> findByCategoryIdAndIsDeletedFalseOrderByArticleNumAsc(Long categoryId);
}