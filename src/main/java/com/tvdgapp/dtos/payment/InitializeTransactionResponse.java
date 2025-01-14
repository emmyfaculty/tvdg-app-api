package com.tvdgapp.dtos.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitializeTransactionResponse {
        private boolean status;
        private String message;
        private Data data;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public class Data {

            private String authorization_url;

            private String access_code;

            private String reference;
        }
    }



