package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
class Content {
    private List<Package> packages;
    private ExportDeclaration exportDeclaration;
    private String unitOfMeasurement;
    @JsonProperty("isCustomsDeclarable")
    private boolean isCustomsDeclarable;
    private String incoterm;
    private String description;
    private String declaredValueCurrency;
    private double declaredValue;

}
