package com.tvdgapp.models.user;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import com.tvdgapp.utils.CommonUtils;
import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

import static com.tvdgapp.constants.SchemaConstant.TABLE_CUSTOMER_REFRESH_TOKEN;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditListener.class)
@Table(name = TABLE_CUSTOMER_REFRESH_TOKEN)

@SuppressWarnings("NullAway.Init")
public class UserRefreshToken extends TvdgAppEntity<Long, UserRefreshToken> implements Auditable {

    public static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String token;

    @Nullable
    private Date validityTrm;

    @Column(nullable = false)
    private String userName;

    @Embedded
    private AuditSection auditSection = new AuditSection();

    public void calculateExpiryDate(String valdtyTrm) {

        if (!"0".equals(valdtyTrm)) { //0 means no validity term used
            if (!CommonUtils.isInteger(valdtyTrm)) {
                this.setDefaultValidityTerm();
            } else {
                if (Integer.parseInt(valdtyTrm) < 0) {
                    this.setDefaultValidityTerm();
                } else {
                    setValidityTrm(DateUtils.addMinutes(TvdgAppDateUtils.now(), Integer.parseInt(valdtyTrm)));
                }
            }
        }

    }

    private void setDefaultValidityTerm() {
        setValidityTrm(DateUtils.addMinutes(TvdgAppDateUtils.now(), EXPIRATION));
    }

    public boolean isExpired() {
        if(this.validityTrm!=null) {
            return this.validityTrm.before(TvdgAppDateUtils.now());
        }
        return false;
    }
}
