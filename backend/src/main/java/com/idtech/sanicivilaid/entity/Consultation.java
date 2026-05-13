package com.idtech.sanicivilaid.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "biz_consultation")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "question_cn", columnDefinition = "TEXT")
    private String questionCn;

    @Column(name = "question_sani", columnDefinition = "TEXT")
    private String questionSani;

    @Column(name = "answer_cn", columnDefinition = "TEXT")
    private String answerCn;

    @Column(name = "answer_sani", columnDefinition = "TEXT")
    private String answerSani;

    @Column(name = "status")
    private Integer status; // 0:未回答, 1:已回答

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Size(max = 50)
    @NotNull
    @Column(name = "sani_font", nullable = false, length = 50)
    private String saniFont;
}