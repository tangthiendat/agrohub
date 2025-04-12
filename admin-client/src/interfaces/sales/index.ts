import { DiscountType } from "../../common/enums";
import { IUserInfo } from "../auth";
import { Auditable } from "../common";
import { ICustomerInfo } from "../customer";
import { IProductBatchInfo, IWarehouseInfo } from "../inventory";
import { IProductInfo, IProductUnitInfo } from "../product";

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

export interface IExportInvoiceDetailBatch {
  exportInvoiceDetailBatchId: string;
  productBatch: IProductBatchInfo;
  quantity: number;
}

export interface IExportInvoiceDetail {
  exportInvoiceDetailId: string;
  product: IProductInfo;
  productUnit: IProductUnitInfo;
  quantity: number;
  unitPrice: number;
  detailBatches: IExportInvoiceDetailBatch[];
}

export interface IExportInvoice extends Auditable {
  exportInvoiceId: string;
  customer: ICustomerInfo;
  warehouse: IWarehouseInfo;
  user: IUserInfo;
  createdDate: string;
  exportInvoiceDetails: IExportInvoiceDetail[];
  totalAmount: number;
  discountValue: number;
  discountType: DiscountType;
  vatRate: number;
  finalAmount: number;
  note?: string;
}

export interface ExportInvoiceFilterCriteria {
  query?: string;
}
