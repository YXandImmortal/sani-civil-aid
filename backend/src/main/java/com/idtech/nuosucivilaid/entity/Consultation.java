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
@Table(name = "biz_consultation")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "question_cn", columnDefinition = "TEXT")
    private String questionCn;

    @Column(name = "question_nuosu", columnDefinition = "TEXT")
    private String questionNuosu;

    @Column(name = "answer_cn", columnDefinition = "TEXT")
    private String answerCn;

    @Column(name = "answer_nuosu", columnDefinition = "TEXT")
    private String answerNuosu;

    @Column(name = "status")
    private Integer status; // 0:未回答, 1:已回答

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}