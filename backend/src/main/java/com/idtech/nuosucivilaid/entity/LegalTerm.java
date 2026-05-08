package com.idtech.nuosucivilaid.entity;

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

    @Column(name = "term_cn", nullable = false)
    private String termCn;

    @Column(name = "term_nuosu", nullable = false)
    private String termNuosu;

    @Column(name = "definition_cn", columnDefinition = "TEXT")
    private String definitionCn;

    @Column(name = "definition_nuosu", columnDefinition = "TEXT")
    private String definitionNuosu;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}