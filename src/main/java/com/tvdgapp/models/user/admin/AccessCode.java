package com.tvdgapp.models.user.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.tvdgapp.constants.SchemaConstant.TABLE_ACCESS_CODE;
import static com.tvdgapp.constants.SchemaConstant.TABLE_ADMIN_USER;

@Getter
@Setter
@NoArgsConstructor
@Entity
//@Table(name = "access_code")
@Table(name = TABLE_ACCESS_CODE)
public class AccessCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "access_code", nullable = false, unique = true)
    private String accessCode;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "ts_created", nullable = false)
    private Integer tsCreated;

    @Column(name = "ts_modified")
    private Integer tsModified;

}
