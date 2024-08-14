//package com.tvdgapp.mapper;
//
//import com.tvdgapp.dtos.shipment.*;
//import com.tvdgapp.models.reference.countrycode.LocaleState;
//import com.tvdgapp.models.shipment.*;
//import com.tvdgapp.repositories.shipment.ShippingOptionRepository;
//import com.tvdgapp.services.reference.states.StatesService;
//import lombok.RequiredArgsConstructor;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@RequiredArgsConstructor
//public class ShipmentUpdateMapper {
//
//    private final ShippingOptionRepository shippingOptionRepository;
//    private final StatesService statesService;
//
//    public static UpdateShipmentResponseDto toEntity(Shipment shipment) {
//        UpdateShipmentResponseDto responseDto = new UpdateShipmentResponseDto();
//        // Create and set sender details
//        responseDto.setTrackingNumber(shipment.getTrackingNumber());
//        responseDto.setCustomerType(shipment.getCustomerType());
//        responseDto.setShipmentType(shipment.getShipmentType());
//        responseDto.setServiceType(shipment.getServiceType());
//
//        // Convert sender details
//        SenderDetails senderDetails = shipment.getSenderDetails();
//        SenderDetailsDto senderDetailsDto = new SenderDetailsDto();
//        senderDetailsDto.setFirstName(senderDetails.getFirstName());
//        senderDetailsDto.setLastName(senderDetails.getLastName());
//        senderDetailsDto.setCompanyName(senderDetails.getCompanyName());
//        senderDetailsDto.setCountry(senderDetails.getCountry());
//        senderDetailsDto.setAddress(senderDetails.getAddress());
//        LocaleState state = statesService.getStateByIso2(senderDetailsDto.getState());
//        senderDetailsDto.setState(senderDetails.getState());
//        senderDetailsDto.setCity(senderDetails.getCity());
//        senderDetailsDto.setZipcode(senderDetails.getZipcode());
//        senderDetailsDto.setEmail(senderDetails.getEmail());
//        senderDetailsDto.setPhoneNo(senderDetails.getPhoneNo());
//        responseDto.setSenderDetails(senderDetailsDto);
//
//        // Convert receiver details
//        ReceiverDetails receiverDetails = shipment.getReceiverDetails();
//        ReceiverDetailsDto receiverDetailsDto = new ReceiverDetailsDto();
//        receiverDetailsDto.setFirstName(receiverDetails.getFirstName());
//        receiverDetailsDto.setLastName(receiverDetails.getLastName());
//        receiverDetailsDto.setCompanyName(receiverDetails.getCompanyName());
//        receiverDetailsDto.setCountry(receiverDetails.getCountry());
//        receiverDetailsDto.setAddress(receiverDetails.getAddress());
//        receiverDetailsDto.setState(receiverDetails.getState());
//        receiverDetailsDto.setCity(receiverDetails.getCity());
//        receiverDetailsDto.setZipcode(receiverDetails.getZipcode());
//        receiverDetailsDto.setEmail(receiverDetails.getEmail());
//        receiverDetailsDto.setPhoneNo(receiverDetails.getPhoneNo());
//        responseDto.setReceiverDetails(receiverDetailsDto);
//
//        responseDto.setTotalNumberOfPackages(shipment.getTotalNumberOfPackages());
//        responseDto.setTotalShipmentValue(shipment.getTotalShipmentValue());
//
//        // Convert dimension
//        Dimension dimension = shipment.getDimension();
//        DimensionDto dimensionDto = new DimensionDto();
//        dimensionDto.setLength(dimension.getLength());
//        dimensionDto.setWidth(dimension.getWidth());
//        dimensionDto.setHeight(dimension.getHeight());
//        responseDto.setDimension(dimensionDto);
//
//        responseDto.setTotalShipmentWeight(shipment.getTotalShipmentWeight());
//        responseDto.setVolumetricWeight(shipment.getVolumetricWeight());
//        responseDto.setPackagingFee(shipment.getPackagingFee());
//        responseDto.setInsuranceFee(shipment.getInsuranceFee());
//        responseDto.setVat(shipment.getVat());
//        responseDto.setTotalShipmentAmount(shipment.getTotalShipmentAmount());
//
//        // Convert product items
//        Set<ProductItemDto> productItemDtos = new HashSet<>();
//        for (ProductItem productItem : shipment.getProductItems()) {
//            ProductItemDto productItemDto = new ProductItemDto();
//            productItemDto.setDescription(productItem.getDescription());
//            productItemDto.setQuantity(productItem.getQuantity());
//            productItemDto.setUnits(productItem.getUnits());
//            productItemDto.setValue(productItem.getValue());
//            productItemDto.setWeight(productItem.getWeight());
//            productItemDto.setManufacturingCountry(productItem.getManufacturingCountry());
//            productItemDto.setPackageCategoryName(productItem.getPackageCategory().getCategoryName());
//            productItemDtos.add(productItemDto);
//        }
//        responseDto.setProductItems(productItemDtos);
//
//        responseDto.setReferralCode(shipment.getReferralCode());
//
//        // Set service option
//        ShippingOption shippingOption = shipment.getShippingOption();
//        responseDto.setShippingOption(shippingOption != null ? shippingOption.getServiceName() : null);
//
//        // Set status with null check
//        ShipmentStatus status = shipment.getStatus();
//        responseDto.setStatus(status != null ? status.name() : null);
//
//
//        // Set customer ID based on customer type
//        if (shipment.getCustomerType() == CustomerType.PARTNER) {
//            responseDto.setCustomerId(shipment.getCustomerUser() != null ? shipment.getCustomerUser().getId() : null);
//        } else {
//            responseDto.setCustomerId(null);
//        }
//
//        return responseDto;
//    }
//
//
//    private static com.tvdgapp.models.shipment.Dimension mapDimension(Dimension dimension) {
//        if (dimension == null) {
//            return null;
//        }
//        dimension.setLength(dimension.getLength());
//        dimension.setWidth(dimension.getWidth());
//        dimension.setHeight(dimension.getHeight());
//        return dimension;
//    }
//}
