package com.tvdgapp.models.wallet;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.tvdgapp.constants.SchemaConstant.TABLE_WALLET;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name=TABLE_WALLET)
public class Wallet extends TvdgAppEntity<Long, Wallet> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "balance", nullable = false, precision = 20, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "wallet_key", nullable = false, unique = true)
    private String walletKey;

    @Embedded
    private AuditSection auditSection = new AuditSection();
    @Override
    public Long getId() {
        return walletId;
    }

    @Override
    public void setId(Long walletId) {
        this.walletId = walletId;
    }
}


