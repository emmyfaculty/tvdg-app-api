package com.tvdgapp.models.fileclaims;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import com.tvdgapp.models.shipment.Shipment;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.boot.autoconfigure.jms.JmsProperties;

import java.util.Date;

import static com.tvdgapp.constants.SchemaConstant.TABLE_FILE_CLAIMS;
import static com.tvdgapp.constants.SchemaConstant.TABLE_SHIPMENT;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SQLDelete(sql =
        "UPDATE shipment " +
                "SET deleted = '1' " +
                "WHERE id = ?")
@Where(clause = "deleted='0'")
@Table(name=TABLE_FILE_CLAIMS)
@SuppressWarnings("NullAway.Init")
public class FileClaims extends TvdgAppEntity<Long, FileClaims> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private boolean packageInsured;
    private String companyName;
    private String contactPersonName;
    private String contactPersonPhoneNo;
    private String contactPersonEmailAddress;
    private String shipmentTrackingNo;
    private String senderName;
    private String originCountry;
    private String receiverName;
    private String destinationCountry;
    private Date shipmentDate;
    private Date expectedDeliveryDate;
    private DeliveryType deliveryTerm;
    private ClaimType claimType;
    private double weightOfDamageGoods;
    private int numberOfDamagePackages;
    private String descriptionOfDamageGoods;
    private String descriptionOfPackaging;
    private boolean availableForInspection;
    private String currencyType;
    private double valueOfTotalShipment;
    private double claimsTotalAmount;
    private String CostBreakDown;
    private TransportInsurance transportInsurance;
    //receipt upload
    @Nullable
    private String uploadReceipt;
    @Nullable
    private String receiptLocalPath;
    @Nullable
    private String receiptUrl;
    private boolean compensatedByOtherParties;
    private FileClaimStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")//todo; make non-null
    private Shipment shipment;
    @Embedded
    private AuditSection auditSection = new AuditSection();
}
