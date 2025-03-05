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
