package com.idtech.nuosucivilaid.repository;

import com.idtech.nuosucivilaid.entity.LegalTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LegalTermRepository extends JpaRepository<LegalTerm, Long> {

    // 搜索术语：支持汉文或彝文关键词
    @Query("SELECT t FROM LegalTerm t WHERE " +
            "(t.termCn LIKE %:keyword% OR t.termNuosu LIKE %:keyword%) " +
            "AND t.isDeleted = false")
    List<LegalTerm> searchTerms(@Param("keyword") String keyword);

    // 获取所有术语（按创建时间排序）
    List<LegalTerm> findByIsDeletedFalseOrderByCreateTimeDesc();
}