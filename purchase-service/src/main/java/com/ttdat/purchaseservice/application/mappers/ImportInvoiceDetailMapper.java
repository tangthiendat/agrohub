package com.ttdat.purchaseservice.application.mappers;

import com.ttdat.purchaseservice.api.dto.common.ImportInvoiceDetailDTO;
import com.ttdat.purchaseservice.domain.entities.ImportInvoiceDetail;
import com.ttdat.purchaseservice.domain.valueobject.EvtImportInvoiceDetail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImportInvoiceDetailMapper extends EntityMapper<ImportInvoiceDetailDTO, ImportInvoiceDetail> {

    ImportInvoiceDetail toEntity(EvtImportInvoiceDetail evtImportInvoiceDetail);

    List<ImportInvoiceDetail> toEntityList(List<EvtImportInvoiceDetail> importInvoiceDetails);
}
