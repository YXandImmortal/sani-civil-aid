package com.idtech.sanicivilaid.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "biz_civil_faq")
public class BizCivilFaq {
    @Id
    @Size(max = 50)
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Size(max = 255)
    @Column(name = "domain")
    private String domain;

    @NotNull
    @Lob
    @Column(name = "question_cn", nullable = false)
    private String questionCn;

    @Lob
    @Column(name = "question_sani")
    private String questionSani;

    @NotNull
    @Lob
    @Column(name = "answer_cn", nullable = false)
    private String answerCn;

    @Lob
    @Column(name = "answer_sani")
    private String answerSani;

    @Size(max = 255)
    @Column(name = "article_refs")
    private String articleRefs;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_time")
    private Instant createTime;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Boolean isDeleted;


}