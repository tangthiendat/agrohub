import { Auditable } from "../common";

export interface IWarehouse extends Auditable {
  warehouseId: string;
  warehouseName: string;
  address?: string;
}
