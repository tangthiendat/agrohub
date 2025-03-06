package com.ttdat.purchaseservice.domain.entities;

import com.ttdat.purchaseservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrder extends Auditable {

    @Id
    String purchaseOrderId;

    Long warehouseId;

    @ManyToOne
    Supplier supplier;

    String userId;

    LocalDate orderDate;

    LocalDate expectedDeliveryDate;

    @Enumerated(EnumType.STRING)
    PurchaseOrderStatus status;

    @Column(precision = 15, scale = 2)
    BigDecimal totalAmount;

    @Column(precision = 15, scale = 2)
    BigDecimal discountValue;

    @Enumerated(EnumType.STRING)
    DiscountType discountType;

    @Column(precision = 15, scale = 2)
    BigDecimal vatRate;

    @Column(precision = 15, scale = 2)
    BigDecimal finalAmount;

    String note;

    @OneToMany(mappedBy = "purchaseOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<PurchaseOrderDetail> purchaseOrderDetails;
}
