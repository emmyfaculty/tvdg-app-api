package com.tvdgapp.models.common.audit;

import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditListener {

    @PrePersist
    public void onSave(Object o) {
        if (o instanceof Auditable) {
            Auditable audit = (Auditable) o;
            AuditSection auditSection = audit.getAuditSection();
            auditSection.setDateModified(TvdgAppDateUtils.now());

            String createdBy = getUsernameOfAuthenticatedUser();
            auditSection.setCreatedBy(createdBy);
            auditSection.setModifiedBy(createdBy);

            if (auditSection.getDateCreated() == null) {
                auditSection.setDateCreated(TvdgAppDateUtils.now());
            }
            audit.setAuditSection(auditSection);
        }
    }

    @PreUpdate
    public void onUpdate(Object o) {
        if (o instanceof Auditable) {
            Auditable audit = (Auditable) o;
            AuditSection auditSection = audit.getAuditSection();
            if (auditSection != null) {
                auditSection.setDateModified(TvdgAppDateUtils.now());

                String modifiedBy = getUsernameOfAuthenticatedUser();
                auditSection.setModifiedBy(modifiedBy);

                if (auditSection.getDateCreated() == null) {
                    auditSection.setDateCreated(TvdgAppDateUtils.now());
                }
                audit.setAuditSection(auditSection);
            }
        }
    }

    private String getUsernameOfAuthenticatedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            if ("anonymousUser".equals(authentication.getPrincipal())) {
                return null;
            }

            Object principal = authentication.getPrincipal();
            if (principal.getClass().equals(String.class)) {
                return (String) principal;
            } else if (principal instanceof SecuredUserInfo) {
                return String.valueOf(((SecuredUserInfo) principal).getUser().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
