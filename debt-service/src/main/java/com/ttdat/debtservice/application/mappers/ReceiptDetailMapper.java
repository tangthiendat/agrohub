package com.ttdat.debtservice.application.mappers;

import com.ttdat.debtservice.domain.entities.ReceiptDetail;
import com.ttdat.debtservice.domain.events.receipt.EvtReceiptDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReceiptDetailMapper {
    @Mapping(target = "debtAccount.debtAccountId", source = "debtAccountId")
    ReceiptDetail toEntity(EvtReceiptDetail evtReceiptDetail);

    List<ReceiptDetail> toEntityList(List<EvtReceiptDetail> evtReceiptDetails);
}