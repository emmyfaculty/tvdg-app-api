package com.tvdgapp.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tvdgapp.models.user.customer.PricingCategory;

import java.io.IOException;

public class PricingCategoryDeserializer extends JsonDeserializer<PricingCategory> {

    @Override
    public PricingCategory deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        String value = jsonParser.getText();
        if (value == null || value.trim().isEmpty()) {
            return PricingCategory.LEVEL_1; // Default value
        }
        return PricingCategory.valueOf(value);
    }
}
