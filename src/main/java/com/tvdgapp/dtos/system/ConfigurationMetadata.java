package com.tvdgapp.dtos.system;

import com.tvdgapp.dtos.common.ItemListingDto;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;

@Data
public class ConfigurationMetadata {
    Collection<ItemListingDto> list= Collections.emptyList();
}
