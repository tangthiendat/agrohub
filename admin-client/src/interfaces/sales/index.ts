import { DiscountType } from "../../common/enums";

export interface CreateDetailBatchLocation {
  batchLocationId: string;
  quantity: number;
}

export interface CreateDetailBatch {
  batchId: string;
  quantity: number;
  detailBatchLocations: CreateDetailBatchLocation[];
}

export interface CreateExportInvoiceDetail {
  productId: string;
  productUnitId: string;
  quantity: number;
  unitPrice: number;
  detailBatches: CreateDetailBatch[];
}

export interface CreateExportInvoiceRequest {
  customerId: string;
  warehouseId: number;
  userId: string;
  createdDate: string;
  totalAmount: number;
  discountValue: number;
  discountType: DiscountType;
  vatRate: number;
  finalAmount: number;
  exportInvoiceDetails: CreateExportInvoiceDetail[];
  note?: string;
}
