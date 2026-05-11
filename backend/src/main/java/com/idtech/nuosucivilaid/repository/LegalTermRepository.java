package com.idtech.nuosucivilaid.repository;

import com.idtech.nuosucivilaid.entity.LegalTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LegalTermRepository extends JpaRepository<LegalTerm, Long> {

    @Query("SELECT t FROM LegalTerm t WHERE " +
            "(t.termCn LIKE %:keyword% OR t.termNuosu LIKE %:keyword%) " +
            "AND t.isDeleted = false ORDER BY t.frequency DESC")
    List<LegalTerm> searchTerms(@Param("keyword") String keyword);

    // 默认按频次降序排列
    List<LegalTerm> findByIsDeletedFalseOrderByFrequencyDesc();
}