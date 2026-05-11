package com.idtech.nuosucivilaid.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "biz_civil_article")
public class CivilArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "article_num", nullable = false)
    private Integer articleNum;

    @Column(name = "content_cn", nullable = false, columnDefinition = "TEXT")
    private String contentCn;

    @Column(name = "content_nuosu", columnDefinition = "TEXT")
    private String contentNuosu;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "update_time")
    private Instant updateTime;
}