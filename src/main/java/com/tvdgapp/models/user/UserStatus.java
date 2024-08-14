package com.tvdgapp.models.user;

import com.tvdgapp.dtos.common.ItemListingDto;

import java.util.ArrayList;
import java.util.Collection;

public enum UserStatus {

    PENDING, ACTIVE, INACTIVE, APPROVED, REJECTED;

    public static Collection<ItemListingDto> toItemList() {
        Collection<ItemListingDto> list = new ArrayList<>();
        for (UserStatus userStatus : UserStatus.values()) {
            list.add(new ItemListingDto(userStatus.name(), userStatus.name()));
        }
        return list;
    }
}
