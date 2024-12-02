package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class ContactInformation {
    private String fullName;
    private String companyName;
    private String email;
    private String phone;
}
