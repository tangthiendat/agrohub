package com.ttdat.salesservice.application.mappers;

import com.ttdat.salesservice.api.dto.common.ExportInvoiceDetailBatchDTO;
import com.ttdat.salesservice.domain.entities.ExportInvoiceDetailBatch;
import com.ttdat.salesservice.domain.valueobject.EvtExportInvoiceDetailBatch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExportInvoiceDetailBatchMapper extends EntityMapper<ExportInvoiceDetailBatchDTO, ExportInvoiceDetailBatch> {
    ExportInvoiceDetailBatch toEntity(EvtExportInvoiceDetailBatch evtExportInvoiceDetailBatch);

    List<ExportInvoiceDetailBatch> toEntityList(List<EvtExportInvoiceDetailBatch> evtExportInvoiceDetailBatches);
}
