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
    @Column(length = 20)
    String purchaseOrderId;

    Long warehouseId;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @Column(length = 50)
    String userId;

    LocalDate orderDate;

    LocalDate expectedDeliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    PurchaseOrderStatus status;

    String note;

    String cancelReason;

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

    @OneToMany(mappedBy = "purchaseOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<PurchaseOrderDetail> purchaseOrderDetails;
}
