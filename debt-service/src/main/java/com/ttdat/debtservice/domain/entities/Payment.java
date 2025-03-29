package com.ttdat.debtservice.domain.entities;

import com.ttdat.debtservice.infrastructure.audit.Auditable;
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
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends Auditable {
    @Id
    @Column(length = 20)
    String paymentId;

    @Column(length = 50, nullable = false)
    String supplierId;

    @Column(nullable = false)
    Long warehouseId;

    @Column(length = 50, nullable = false)
    String userId;

    @Column(nullable = false)
    LocalDate createdDate;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal totalPaidAmount;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    PaymentMethod paymentMethod;

    String note;

    @OneToMany(mappedBy = "payment", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<PaymentDetail> paymentDetails;
}
