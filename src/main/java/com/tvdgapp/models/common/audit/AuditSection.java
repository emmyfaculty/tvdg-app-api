package com.tvdgapp.models.common.audit;

import com.tvdgapp.utils.CloneUtils;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Embeddable
@SuppressWarnings("NullAway.Init")
public class AuditSection implements Serializable {

    @Serial
    private static final long serialVersionUID = 1934446958975060889L;

    @Column(name = "DATE_CREATED")//todo: make non nullable
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Column(name = "DATE_MODIFIED")//todo: make non nullable
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModified;

    @Column(name = "modified_by")//todo: make non nullable
    private String modifiedBy;

    @Column(name = "created_by")//todo: make non nullable
    private String createdBy;


    @Column(name = "DELETED", columnDefinition = "varchar(1)")//todo: make non nullable
    private String delF = "0";

    @Column(name = "ts_created")
    @Temporal(TemporalType.TIMESTAMP)
    private long tsCreated;

    @Column(name = "ts_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private long tsModified;

    public AuditSection() {
    }

    @Nullable
    public Date getDateCreated() {
        return CloneUtils.clone(dateCreated);
    }

//    public void setDateCreated(Date dateCreated) {
//        var dateClone = CloneUtils.clone(dateCreated);
//        if (dateClone != null) {
//            this.dateCreated = dateClone;
//        }
//    }

    public void setDateCreated(Date dateCreated) {
        var dateClone = CloneUtils.clone(dateCreated);
        if (dateClone != null) {
            this.dateCreated = dateClone;
            this.tsCreated = dateClone.getTime() / 1000;
        }
    }

    public @Nullable Date getDateModified() {
        return CloneUtils.clone(dateModified);
    }

//    public void setDateModified(Date dateModified) {
//        var dateClone = CloneUtils.clone(dateCreated);
//        if (dateClone != null) {
//            this.dateModified = dateClone;
//        }
//    }


    public void setDateModified(Date dateModified) {
        var dateClone = CloneUtils.clone(dateModified);
        if (dateClone != null) {
            this.dateModified = dateClone;
            this.tsModified = dateClone.getTime() / 1000;
        }
    }

    @Nullable
    public String getCreatedBy() {
        return createdBy;
    }

    @Nullable
    public String getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public String toString() {
        return "AuditSection{" +
                "dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", delF='" + delF + '\'' +
                ", tsCreated=" + tsCreated +
                ", tsModified=" + tsModified +
                '}';
    }
}
