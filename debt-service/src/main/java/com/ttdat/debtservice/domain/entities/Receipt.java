package com.ttdat.debtservice.domain.entities;

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
@Table(name = "receipts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Receipt {
    @Id
    @Column(length = 20)
    String receiptId;

    @Column(length = 50, nullable = false)
    String customerId;

    @Column(nullable = false)
    Long warehouseId;

    @Column(length = 50, nullable = false)
    String userId;

    @Column(nullable = false)
    LocalDate createdDate;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal totalReceivedAmount;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    PaymentMethod paymentMethod;

    String note;

    @OneToMany(mappedBy = "receipt", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<ReceiptDetail> receiptDetails;
}
