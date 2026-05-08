package com.idtech.nuosucivilaid.repository;

import com.idtech.nuosucivilaid.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    // 获取指定用户的咨询记录，按时间倒序
    List<Consultation> findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(Long userId);
}