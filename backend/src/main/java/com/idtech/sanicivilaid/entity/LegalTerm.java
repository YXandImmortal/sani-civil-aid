package com.idtech.sanicivilaid.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "biz_legal_term")
public class LegalTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "frequency")
    private Integer frequency; // 新增：频次

    @Column(name = "term_cn", nullable = false)
    private String termCn;

    @Column(name = "term_sani", nullable = false)
    private String termSani;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}