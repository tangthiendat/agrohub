import { DiscountType, PurchaseOrderStatus } from "../../common/enums";
import { IUserInfo } from "../auth";
import { Auditable } from "../common";
import { IWarehouse } from "../inventory";
import { IProduct } from "../product";

export interface ISupplier extends Auditable {
  supplierId: string;
  supplierName: string;
  email: string;
  phoneNumber: string;
  active: boolean;
  address?: string;
  taxCode?: string;
  contactPerson?: string;
  notes?: string;
}

export interface SupplierFilterCriteria {
  query?: string;
  active?: boolean;
}

export interface ISupplierProduct {
  supplierProductId: string;
  supplier: ISupplier;
  productId: string;
}

export interface IPurchaseOrderDetail {
  detailId: string;
  product: IProduct;
  productUnitId: number;
  quantity: number;
}

export interface IPurchaseOrder extends Auditable {
  purchaseOrderId: string;
  supplier: ISupplier;
  warehouse: IWarehouse;
  user: IUserInfo;
  orderDate: string;
  expectedDeliveryDate: string;
  status: PurchaseOrderStatus;
  totalAmount: number;
  discountValue: number;
  discountType: DiscountType;
  vatRate: number;
  finalAmount: number;
  purchaseOrderDetails: IPurchaseOrderDetail[];
}

export interface CreatePurchaseOrderDetail {
  productId: string;
  productUnitId: string;
  quantity: number;
}

export interface CreatePurchaseOrderRequest {
  supplierId: string;
  warehouseId: number;
  userId: string;
  orderDate: string;
  status: PurchaseOrderStatus;
  expectedDeliveryDate: string;
  totalAmount: number;
  discountValue: number;
  discountType: DiscountType;
  vatRate: number;
  finalAmount: number;
  purchaseOrderDetails: CreatePurchaseOrderDetail[];
  note?: string;
}

export interface IPurchaseOrderListItem {
  purchaseOrderId: string;
  supplierName: string;
  orderDate: string;
  expectedDeliveryDate: string;
  status: PurchaseOrderStatus;
}

export interface PurchaseOrderFilterCriteria {
  query?: string;
  status?: string;
}
