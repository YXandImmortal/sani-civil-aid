package com.idtech.sanicivilaid.repository;

import com.idtech.sanicivilaid.entity.LegalTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface LegalTermRepository extends JpaRepository<LegalTerm, Long> {

    @Query("SELECT t FROM LegalTerm t WHERE " +
            "(t.termCn LIKE %:keyword% OR t.termSani LIKE %:keyword%) " +
            "AND t.isDeleted = false ORDER BY t.frequency DESC")
    List<LegalTerm> searchTerms(@Param("keyword") String keyword, Pageable pageable);

    // 默认按频次降序排列
    List<LegalTerm> findByIsDeletedFalseOrderByFrequencyDesc(Pageable pageable);
}