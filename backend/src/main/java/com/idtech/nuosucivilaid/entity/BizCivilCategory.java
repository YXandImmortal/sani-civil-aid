package com.idtech.nuosucivilaid.entity;

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
@Table(name = "biz_civil_category")
public class BizCivilCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ColumnDefault("0")
    @Column(name = "parent_id")
    private Long parentId;

    @Size(max = 100)
    @NotNull
    @Column(name = "name_cn", nullable = false, length = 100)
    private String nameCn;

    @Size(max = 100)
    @Column(name = "name_nuosu", length = 100)
    private String nameNuosu;

    @ColumnDefault("0")
    @Column(name = "sort")
    private Integer sort;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Boolean isDeleted;


}