package com.tvdgapp.dtos.dhl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
class OutputImageProperties {
    private boolean allDocumentsInOneImage;
    private String encodingFormat;
    private List<ImageOption> imageOptions;
}