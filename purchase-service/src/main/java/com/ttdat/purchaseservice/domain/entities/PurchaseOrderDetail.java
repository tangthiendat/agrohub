package com.ttdat.purchaseservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_order_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderDetail {

    @Id
    @Column(length = 50)
    String purchaseOrderDetailId;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    PurchaseOrder purchaseOrder;

    @Column(length = 100)
    String productId;

    @Column(length = 50)
    String productUnitId;

    Integer quantity;

    @Column(precision = 15, scale = 2)
    BigDecimal unitPrice;
}
