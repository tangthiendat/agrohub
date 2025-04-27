package com.ttdat.purchaseservice.domain.entities;

import com.ttdat.purchaseservice.infrastructure.audit.Auditable;
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
@Table(name = "suppliers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier extends Auditable {

    @Id
    @Column(length = 50)
    String supplierId;

    @Column(length = 150, nullable = false)
    String supplierName;

    @Column(nullable = false)
    String email;

    @Column(length = 20, nullable = false)
    String phoneNumber;

    boolean active;

    String address;

    @Column(length = 20)
    String taxCode;

    @Column(length = 100)
    String contactPerson;

    @Column(length = 500)
    String notes;

    @OneToMany(mappedBy = "supplier", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    List<SupplierProduct> supplierProducts;

    @OneToMany(mappedBy = "supplier")
    List<PurchaseOrder> purchaseOrders;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER)
    List<SupplierRating> supplierRatings;
}
