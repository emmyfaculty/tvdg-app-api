package com.tvdgapp.models.shipment.nationwide;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.tvdgapp.constants.SchemaConstant.TABLE_NATIONAL_WIDE_STATE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name= TABLE_NATIONAL_WIDE_STATE)
@Entity
public class NationWideState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nation_wide_region_id")
    private NationWideRegion region;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NationWideCity> cities;
    private  String description;

}
