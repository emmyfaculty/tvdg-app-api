package com.tvdgapp.dtos.user.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerCseMapDTO {

    private Long id;
    private Long customerId;
    private Long cseId;
    private Long assignedBy;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private Long lastModifiedBy;
    private Integer tsCreated;
    private Integer tsModified;
    private String deleted;

}
