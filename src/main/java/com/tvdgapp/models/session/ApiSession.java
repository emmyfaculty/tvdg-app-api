package com.tvdgapp.models.session;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static com.tvdgapp.constants.SchemaConstant.TABLE_API_SESSION;
import static com.tvdgapp.constants.SchemaConstant.TABLE_QUOTE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name=TABLE_API_SESSION)
public class ApiSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "device_id")
    private String deviceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type")
    private DeviceType deviceType;

    @Column(name = "os")
    private String os;

    @Column(name = "browser")
    private String browser;

    @Column(name = "location")
    private String location;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "expires_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Column(name = "last_used_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUsedAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "revoked_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date revokedAt;

    @Column(name = "revoked_reason")
    private String revokedReason;

    @Column(name = "session_duration")
    private Integer sessionDuration;

}
