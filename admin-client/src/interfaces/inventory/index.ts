import { LocationStatus, RackType } from "../../common/enums";
import { Auditable } from "../common";
import { IProductInfo } from "../product";

export interface IWarehouse extends Auditable {
  warehouseId: number;
  warehouseName: string;
  address: string;
}

export interface IWarehouseInfo {
  warehouseId: number;
  warehouseName: string;
  address: string;
}

export interface IProductLocation extends Auditable {
  locationId: string;
  warehouseId: number;
  rackName: string;
  rackType: RackType;
  rowNumber: number;
  columnNumber: number;
  status: LocationStatus;
  reason?: string;
}

export interface ProductLocationFilterCriteria {
  status?: string;
  rackType?: string;
  query?: string;
}

export interface IProductBatchLocation {
  batchLocationId?: string;
  productLocation: IProductLocation;
  quantity: number;
}

export interface IProductBatch extends Auditable {
  batchId: string;
  product: IProductInfo;
  manufacturingDate: string;
  expirationDate: string;
  receivedDate: string;
  quantity: number;
  batchLocations: IProductBatchLocation[];
}

export interface IProductBatchInfo {
  batchId: string;
  product: IProductInfo;
  manufacturingDate: string;
  expirationDate: string;
  receivedDate: string;
  quantity: number;
}

export interface ProductBatchFilterCriteria {
  query?: string;
  isArranged?: boolean;
}

export interface IProductStock extends Auditable {
  productStockId: string;
  product: IProductInfo;
  quantity: number;
}

export interface ProductStockFilterCriteria {
  query?: string;
  categoryId?: string;
}
