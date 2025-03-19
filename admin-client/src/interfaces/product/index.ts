import { PhysicalState } from "../../common/enums";
import { Auditable } from "../common";

export interface IUnit extends Auditable {
  unitId: number;
  unitName: string;
  description: string;
}

export interface IUnitInfo {
  unitId: number;
  unitName: string;
}

export interface ICategory extends Auditable {
  categoryId: number;
  categoryName: string;
}

export interface ICategoryInfo {
  categoryId: number;
  categoryName: string;
}

export interface IProductUnitPrice {
  productUnitPriceId: string;
  price: number;
  validFrom: string;
  validTo?: string;
}

export interface IProductUnit {
  productUnitId: string;
  unit: IUnit;
  conversionFactor: number;
  isDefault: boolean;
  productUnitPrices: IProductUnitPrice[];
}

export interface IProduct extends Auditable {
  productId: string;
  productName: string;
  description?: string;
  totalQuantity?: number;
  imageUrl?: string;
  category: ICategory;
  defaultExpDays: number;
  storageInstructions?: string;
  packaging?: string;
  physicalState?: PhysicalState;
  safetyInstructions?: string;
  ppeRequired?: string;
  productUnits: IProductUnit[];
}

export interface ProductFilterCriteria {
  query?: string;
  categoryId?: number;
}

export interface IProductInfo {
  productId: string;
  productName: string;
  category: ICategoryInfo;
  unit: IUnitInfo;
  imageUrl: string;
}
