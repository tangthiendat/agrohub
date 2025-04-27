package com.ttdat.debtservice.application.mappers;

import com.ttdat.debtservice.domain.entities.Receipt;
import com.ttdat.debtservice.domain.events.receipt.ReceiptCreatedEvent;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR,
        uses = {ReceiptDetailMapper.class})
public interface ReceiptMapper {
    @Mapping(target = "paymentMethod.paymentMethodId", source = "paymentMethodId")
    Receipt toEntity(ReceiptCreatedEvent receiptCreatedEvent);

    @AfterMapping
    default void setReceiptIdDetails(@MappingTarget Receipt receipt, ReceiptCreatedEvent receiptCreatedEvent) {
        if (receipt.getReceiptDetails() != null) {
            receipt.getReceiptDetails().forEach(receiptDetail ->
                    receiptDetail.setReceipt(Receipt.builder().receiptId(receipt.getReceiptId()).build()));
        }
    }
}