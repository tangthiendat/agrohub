import { LocationStatus, RackType } from "../../common/enums";
import { Auditable } from "../common";

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

export interface CreateProductLocationRequest {
  warehouseId: number;
  rackName: string;
  rackType: RackType;
  rowNumber: number;
  columnNumber: number;
}

export interface UpdateProductLocationRequest {
  warehouseId: number;
  rackName: string;
  rackType: RackType;
  rowNumber: number;
  columnNumber: number;
}

export interface IProductLocation extends Auditable {
  warehouse: IWarehouseInfo;
  rackName: string;
  rackType: RackType;
  rowNumber: number;
  columnNumber: number;
  status: LocationStatus;
}
