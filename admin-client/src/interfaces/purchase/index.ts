import { Auditable } from "../common";

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

export interface ISupplierProductQuotation {
  quotationId: string;
  unitId: number;
  unitPrice: number;
  validFrom: string;
  validTo?: string;
}
