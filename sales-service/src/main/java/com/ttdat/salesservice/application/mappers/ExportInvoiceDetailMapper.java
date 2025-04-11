package com.ttdat.salesservice.application.mappers;

import com.ttdat.salesservice.domain.entities.ExportInvoiceDetail;
import com.ttdat.salesservice.domain.valueobject.EvtExportInvoiceDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ExportInvoiceDetailBatchMapper.class})
public interface ExportInvoiceDetailMapper {
    List<ExportInvoiceDetail> toEntityList(List<EvtExportInvoiceDetail> evtExportInvoiceDetails);

    ExportInvoiceDetail toEntity(EvtExportInvoiceDetail evtExportInvoiceDetail);

    @AfterMapping
    default void setExportInvoiceDetailBatchForDetails(@MappingTarget ExportInvoiceDetail exportInvoiceDetail, EvtExportInvoiceDetail evtExportInvoiceDetail) {
        if (exportInvoiceDetail.getDetailBatches() != null) {
            exportInvoiceDetail.getDetailBatches().forEach(exportInvoiceDetailBatch ->
                    exportInvoiceDetailBatch.setExportInvoiceDetail(
                            ExportInvoiceDetail.builder()
                                    .exportInvoiceDetailId(exportInvoiceDetail.getExportInvoiceDetailId())
                                    .build()));
        }
    }
}
