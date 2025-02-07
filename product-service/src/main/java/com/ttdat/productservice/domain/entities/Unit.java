package com.ttdat.productservice.domain.entities;

import com.ttdat.productservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "units")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Unit extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long unitId;

    String unitName;

    @OneToMany(mappedBy = "unit")
    List<ProductUnit> productUnits;
}
