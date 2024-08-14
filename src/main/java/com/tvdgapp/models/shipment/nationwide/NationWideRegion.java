package com.tvdgapp.models.shipment.nationwide;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.tvdgapp.constants.SchemaConstant.TABLE_NATIONAL_WIDE_REGION;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name= TABLE_NATIONAL_WIDE_REGION)
@Entity
public class NationWideRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NationWideState> states;
    private String description;

}
