package com.tvdgapp.models.user;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static com.tvdgapp.constants.SchemaConstant.TABLE_TOKEN_BLACKLIST;


@Entity
@Getter
@Setter
@Table(name = TABLE_TOKEN_BLACKLIST)
@SuppressWarnings("NullAway.Init")
public class TokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "mediumtext")
    @Nullable
    private String token;
}