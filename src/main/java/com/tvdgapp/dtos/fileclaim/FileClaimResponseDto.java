package com.tvdgapp.dtos.fileclaim;

import com.tvdgapp.models.fileclaims.ClaimType;
import com.tvdgapp.models.fileclaims.DeliveryType;
import com.tvdgapp.models.fileclaims.FileClaimStatus;
import com.tvdgapp.models.fileclaims.TransportInsurance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileClaimResponseDto {

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
    private String documentUpload;
    private boolean compensatedByOtherParties;
    private FileClaimStatus status;

}
