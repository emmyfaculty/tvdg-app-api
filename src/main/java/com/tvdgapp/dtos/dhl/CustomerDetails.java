package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class CustomerDetails {
    private ShipperDetails shipperDetails;
    private ReceiverDetails receiverDetails;

}