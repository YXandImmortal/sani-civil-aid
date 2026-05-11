package com.idtech.nuosucivilaid.repository;

import com.idtech.nuosucivilaid.entity.BizCivilFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BizCivilFaqRepository extends JpaRepository<BizCivilFaq, String> {

    @Query("SELECT f FROM BizCivilFaq f WHERE " +
            "(f.questionCn LIKE %:keyword% OR f.answerCn LIKE %:keyword%) " +
            "AND f.isDeleted = false")
    List<BizCivilFaq> searchByCnKeyword(@Param("keyword") String keyword);

    @Query("SELECT f FROM BizCivilFaq f WHERE " +
            "(f.questionNuosu LIKE %:keyword% OR f.answerNuosu LIKE %:keyword%) " +
            "AND f.isDeleted = false")
    List<BizCivilFaq> searchByNuosuKeyword(@Param("keyword") String keyword);
}
