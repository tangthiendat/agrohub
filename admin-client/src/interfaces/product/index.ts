import { Auditable } from "../common";

export interface IUnit extends Auditable {
  unitId: number;
  unitName: string;
  description: string;
}

export interface ICategory extends Auditable {
  categoryId: number;
  categoryName: string;
}
