package com.tvdgapp.models.common.audit;

public interface Auditable {

    AuditSection getAuditSection();

    void setAuditSection(AuditSection audit);
}
