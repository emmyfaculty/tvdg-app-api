package com.tvdgapp.services.fileclaim;

import com.tvdgapp.constants.Constants;
import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.dtos.fileclaim.FileClaimRequestDto;
import com.tvdgapp.dtos.fileclaim.FileClaimResponseDto;
import com.tvdgapp.exceptions.DuplicateEntityException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.fileclaims.*;
import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.repositories.fileclaim.FileClaimRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.services.shipment.international.ShipmentService;
import com.tvdgapp.services.storage.FileStorageService;
import com.tvdgapp.services.system.SystemConfigurationService;
import com.tvdgapp.utils.EmailTemplateUtils;
import com.tvdgapp.utils.FilePathUtils;
import com.tvdgapp.utils.FileUploadValidatorUtils;
import com.tvdgapp.utils.TvdgAppDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

import static com.tvdgapp.constants.SchemaConstant.FILE_CLAIM_REQUEST_TO_EMAIL;
import static com.tvdgapp.exceptions.EntityType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileClaimServiceImpl extends TvdgEntityServiceImpl<Long, FileClaims> implements FileClaimService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileClaimServiceImpl.class);


    private final FileClaimRepository fileClaimRepository;
    private final ShipmentService shipmentService;
    private final FileStorageService fileStorageService;
    private final SystemConfigurationService configurationService;
    private final EmailTemplateUtils emailTemplateUtils;

    @Override
    @Transactional
    public FileClaimResponseDto createFileClaim(FileClaimRequestDto requestDto ) {
        Optional<Shipment> shipment = this.shipmentService.findByTrackingNo(requestDto.getShipmentTrackingNo());
        if (shipment.isEmpty()) {
            throw new ResourceNotFoundException(SHIPMENT, String.valueOf(requestDto.getShipmentTrackingNo()));
        }
        fileClaimRepository.findByShipmentTrackingNo(requestDto.getShipmentTrackingNo())
                .ifPresent(existingClaim -> {
//                    throw new DuplicateEntityException("File claim with tracking number " + requestDto.getShipmentTrackingNo() + " already exists");
                    throw new DuplicateEntityException(FILE_CLAIM.name(), " with tracking number ", requestDto.getShipmentTrackingNo() + " already exist");
                });

        FileClaims fileClaims = new FileClaims();

        fileClaims.setShipment(shipment.get());
        // Map the request DTO to an entity
        this.mapRequestDtoToFileClaim(requestDto, fileClaims);

        // Save the entity to the database
        fileClaims = this.savedFileClaim(fileClaims);
        this.sendFileClaimEmailNotification(fileClaims);

        // Map the saved entity back to a response DTO
        FileClaimResponseDto responseDto = mapFileClaimToResponseDto(fileClaims);

        return responseDto;


    }

    @Override
    @Transactional
    public Optional<String> uploadShipmentReceipt(long fileClaimsId, MultipartFile uploadReceipt) {
        Optional<FileClaims> optionalFileClaims = this.fileClaimRepository.findById(fileClaimsId);
        if (optionalFileClaims.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_USER, String.valueOf(fileClaimsId));
        }
        return storeFile(optionalFileClaims.get(),uploadReceipt);
    }

    private Optional<String> storeFile(FileClaims fileClaims, MultipartFile receipt) {
        if (FileUploadValidatorUtils.isFileUploaded(receipt)) {
            try {
                //build file path
                String filename = FilePathUtils.buildUniqueFileName(receipt);
                String filePath = FilePathUtils.buildFileClaimsReceiptUploadPath();
                String fileNamePath = filePath + File.separator + filename;
                //if edit mode delete existing file
                if(fileClaims.getUploadReceipt()!=null) {
                    this.fileStorageService.deleteFile(filePath+File.separator+fileClaims.getUploadReceipt());
                }
                //store file
                this.fileStorageService.storeFile(receipt, fileNamePath);
                //add or update record
                fileClaims.setUploadReceipt(filename);
                return Optional.of(this.fileStorageService.getStorageLocation()+File.separator+fileNamePath);
            } catch (Exception e) {
                LOGGER.error("Unable to store uploaded profile pic", e);
            }
        }
        return Optional.empty();
    }


//    private Optional<String> storeFiles(FileClaims fileClaims, MultipartFile receipt) {
//
//        Shipment shipment = fileClaims.getShipment();
//        String filename, fileDir, url, fileNamePath;
//        //store uploaded files
//        //receipt
//        if (FileUploadValidatorUtils.isFileUploaded(receipt)) {
//            try {
//                //build file path
//                filename = FilePathUtils.buildUniqueFileName(receipt);
//                fileDir = FilePathUtils.buildOrganizationFileLocation(shipment, Constants.SUBSCRIPTION_RECEIPTS_DIR);
//                fileNamePath = fileDir + File.separator + filename;
//
//                //if edit mode delete existing file
//                if (fileClaims.getUploadReceipt() != null) {
//                    this.fileStorageService.deleteFile(fileDir + File.separator + fileClaims.getUploadReceipt());
//                }
//                //store file
//                url = this.fileStorageService.storeFile(receipt, fileNamePath);
//
//                //add or update record
//                fileClaims.setReceiptUrl(url);
//                //subscription.setReceiptLocalPath(fileNamePath);
//                fileClaims.setUploadReceipt(filename);
//
//            } catch (Exception e) {
//                LOGGER.error("Unable to store uploaded receipt", e);
//            }
//        }
//        return Optional.empty();
//    }


    private FileClaims savedFileClaim (FileClaims fileClaims){
        return this.fileClaimRepository.save(fileClaims);
    }

    private void sendFileClaimEmailNotification(FileClaims fileClaims) {

        String toEmail = this.configurationService.findConfigValueByKey(FILE_CLAIM_REQUEST_TO_EMAIL);
        if (StringUtils.isEmpty(toEmail)) {
            toEmail = SchemaConstant.DEFAULT_COMPANY_EMAIL;
        }
        try {
            this.emailTemplateUtils.sendFileClaimRequestEmail(toEmail, fileClaims);
        } catch (Exception e) {
            LOGGER.error("Cannot send file claim request email", e);
        }
    }


    private FileClaims mapRequestDtoToFileClaim(FileClaimRequestDto requestDto, FileClaims fileClaim) {
        fileClaim.setPackageInsured(requestDto.isPackageInsured());
        fileClaim.setCompanyName(requestDto.getCompanyName());
        fileClaim.setContactPersonName(requestDto.getContactPersonName());
        fileClaim.setContactPersonPhoneNo(requestDto.getContactPersonPhoneNo());
        fileClaim.setContactPersonEmailAddress(requestDto.getContactPersonEmailAddress());
        fileClaim.setSenderName(requestDto.getSenderName());
        fileClaim.setOriginCountry(requestDto.getOriginCountry());
        fileClaim.setReceiverName(requestDto.getReceiverName());
        fileClaim.setDestinationCountry(requestDto.getDestinationCountry());
        fileClaim.setShipmentTrackingNo(requestDto.getShipmentTrackingNo());
        fileClaim.setShipmentDate(TvdgAppDateUtils.formatStringToDate(String.valueOf(requestDto.getShipmentDate()), DateConstants.DATE_INPUT_FORMAT));
        fileClaim.setExpectedDeliveryDate(TvdgAppDateUtils.formatStringToDate(String.valueOf(requestDto.getExpectedDeliveryDate()), DateConstants.DATE_INPUT_FORMAT));
        fileClaim.setDeliveryTerm(DeliveryType.valueOf(requestDto.getDeliveryType()));
        fileClaim.setClaimType(ClaimType.valueOf(requestDto.getClaimType()));
        fileClaim.setWeightOfDamageGoods(requestDto.getWeightOfDamageGoods());
        fileClaim.setNumberOfDamagePackages(requestDto.getNumberOfDamagePackages());
        fileClaim.setDescriptionOfDamageGoods(requestDto.getDescriptionOfDamageGoods());
        fileClaim.setDescriptionOfPackaging(requestDto.getDescriptionOfPackaging());
        fileClaim.setAvailableForInspection(requestDto.isAvailableForInspection());
        fileClaim.setCurrencyType(requestDto.getCurrencyType());
        fileClaim.setValueOfTotalShipment(requestDto.getValueOfTotalShipment());
        fileClaim.setClaimsTotalAmount(requestDto.getClaimsTotalAmount());
        fileClaim.setCostBreakDown(requestDto.getCostBreakDown());
        fileClaim.setTransportInsurance(requestDto.getTransportInsurance());
        fileClaim.setCompensatedByOtherParties(requestDto.isCompensatedByOtherParties());
        fileClaim.setStatus(FileClaimStatus.IN_PROGRESS);
        return fileClaim;
    }

    private TransportInsurance mapTransportInsurance(String transportInsuranceString) {

        if (transportInsuranceString.equalsIgnoreCase("Toladol cargo insurance")) {
            return TransportInsurance.TOLADOL_CARGO_INSURANCE;
        } else if (transportInsuranceString.equalsIgnoreCase("through another insurance company")) {
            return TransportInsurance.ANOTHER_INSURANCE_COMPANY;
        } else {
            return TransportInsurance.NONE;
        }
    }
    private ClaimType mapClaimType(String claimType) {

        if (claimType.equalsIgnoreCase("Lost shipment")) {
            return ClaimType.LOST_SHIPMENT;
        } else if (claimType.equalsIgnoreCase("Damage shipment")) {
            return ClaimType.DAMAGE_SHIPMENT;
        } else if (claimType.equalsIgnoreCase("Lost item")) {
            return ClaimType.LOST_ITEM;
        } else {
            return ClaimType.DAMAGE_ITEM;
        }
    }
    private DeliveryType mapDeliveryType(String deliveryType) {

        if (deliveryType.equalsIgnoreCase("door step delivery")) {
            return DeliveryType.DOOR_STEP_DELIVERY;
        } else if (deliveryType.equalsIgnoreCase("access point")) {
            return DeliveryType.ACCESS_POINT;
        } else {
            return DeliveryType.OTHERS;
        }
    }

    private FileClaimResponseDto mapFileClaimToResponseDto( FileClaims fileClaim) {
        FileClaimResponseDto responseDto = new FileClaimResponseDto();
        responseDto.setPackageInsured(fileClaim.isPackageInsured());
        responseDto.setCompanyName(fileClaim.getCompanyName());
        responseDto.setContactPersonName(fileClaim.getContactPersonName());
        responseDto.setContactPersonPhoneNo(fileClaim.getContactPersonPhoneNo());
        responseDto.setContactPersonEmailAddress(fileClaim.getContactPersonEmailAddress());
        responseDto.setShipmentTrackingNo(fileClaim.getShipment().getTrackingNumber());
        responseDto.setSenderName(fileClaim.getSenderName());
        responseDto.setOriginCountry(fileClaim.getOriginCountry());
        responseDto.setReceiverName(fileClaim.getReceiverName());
        responseDto.setDestinationCountry(fileClaim.getDestinationCountry());
        responseDto.setShipmentDate(fileClaim.getShipmentDate());
        responseDto.setExpectedDeliveryDate(fileClaim.getExpectedDeliveryDate());
        responseDto.setDeliveryTerm(fileClaim.getDeliveryTerm());
        responseDto.setClaimType(fileClaim.getClaimType());
        responseDto.setWeightOfDamageGoods(fileClaim.getWeightOfDamageGoods());
        responseDto.setNumberOfDamagePackages(fileClaim.getNumberOfDamagePackages());
        responseDto.setDescriptionOfDamageGoods(fileClaim.getDescriptionOfDamageGoods());
        responseDto.setDescriptionOfPackaging(fileClaim.getDescriptionOfPackaging());
        responseDto.setAvailableForInspection(fileClaim.isAvailableForInspection());
        responseDto.setCurrencyType(fileClaim.getCurrencyType());
        responseDto.setValueOfTotalShipment(fileClaim.getValueOfTotalShipment());
        responseDto.setClaimsTotalAmount(fileClaim.getClaimsTotalAmount());
        responseDto.setCostBreakDown(fileClaim.getCostBreakDown());
        responseDto.setTransportInsurance(fileClaim.getTransportInsurance());
        responseDto.setDocumentUpload(fileClaim.getUploadReceipt());
        responseDto.setCompensatedByOtherParties(fileClaim.isCompensatedByOtherParties());
        responseDto.setStatus(fileClaim.getStatus());
        return responseDto;
    }

    private String mapTransportInsuranceToString(TransportInsurance transportInsurance) {
        if (transportInsurance == null) {
            return null;
        }
        switch (transportInsurance) {
            case TOLADOL_CARGO_INSURANCE:
                return "Toladol cargo insurance";
            case ANOTHER_INSURANCE_COMPANY:
                return "Through another insurance company";
            case NONE:
                return "None";
            default:
                throw new IllegalArgumentException("Unsupported transport insurance: " + transportInsurance);
        }
    }

    private String mapClaimTypeToString(ClaimType claimType) {
        if (claimType == null) {
            return null;
        }
        switch (claimType) {
            case LOST_SHIPMENT:
                return "Lost shipment";
            case DAMAGE_SHIPMENT:
                return "Damage shipment";
            case LOST_ITEM:
                return "Lost item";
            case DAMAGE_ITEM:
                return "Damage item";
            default:
                throw new IllegalArgumentException("Unsupported claim type: " + claimType);
        }
    }

    private String mapDeliveryTypeToString(DeliveryType deliveryType) {
        if (deliveryType == null) {
            return null;
        }
        switch (deliveryType) {
            case DOOR_STEP_DELIVERY:
                return "door step delivery";
            case ACCESS_POINT:
                return "access point";
            case OTHERS:
                return "others";
            default:
                throw new IllegalArgumentException("Unsupported delivery type: " + deliveryType);
        }
    }

}
