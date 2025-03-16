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

export interface IProductLocation extends Auditable {
  locationId: number;
  warehouseId: number;
  rackName: string;
  rackType: RackType;
  rowNumber: number;
  columnNumber: number;
  status: LocationStatus;
}

export interface ProductLocationFilterCriteria {
  status?: string;
  rackType?: string;
  query?: string;
}
