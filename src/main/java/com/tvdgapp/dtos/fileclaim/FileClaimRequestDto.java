package com.tvdgapp.dtos.fileclaim;

import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.models.fileclaims.ClaimType;
import com.tvdgapp.models.fileclaims.DeliveryType;
import com.tvdgapp.models.fileclaims.FileClaimStatus;
import com.tvdgapp.models.fileclaims.TransportInsurance;
import com.tvdgapp.utils.annotation.ExcelColumn;
import com.tvdgapp.validators.CustomDate;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileClaimRequestDto {

    private boolean packageInsured;
    private String companyName;
    private String contactPersonName;
    private String contactPersonPhoneNo;
    private String contactPersonEmailAddress;
    private String senderName;
    private String originCountry;
    private String receiverName;
    private String destinationCountry;
    private String shipmentTrackingNo;
    @Nullable
//    @Schema(
//            description = "Date format: " + DateConstants.DATE_INPUT_FORMAT,
//            example = "05-05-1995"
//
//    )
    @CustomDate(message="Date not in valid format")
    @ExcelColumn(name = "shipment date", col = 8,errMsg = "Invalid Date Of Birth- {val},expected format ["+DateConstants.DATE_INPUT_FORMAT+"]")
    private String shipmentDate;
    @Nullable
//    @Schema(
//            description = "Date format: " + DateConstants.DATE_INPUT_FORMAT,
//            example = "05-05-1995"
//
//    )
    @CustomDate(message="Date not in valid format")
    @ExcelColumn(name = "expected delivery date", col = 8,errMsg = "Invalid Date Of Birth- {val},expected format ["+DateConstants.DATE_INPUT_FORMAT+"]")
    private String expectedDeliveryDate;
    private String deliveryType;
    private String claimType;
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
    private String status;

}
