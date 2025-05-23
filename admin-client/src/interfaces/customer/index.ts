import { CustomerType } from "../../common/enums";
import { Auditable } from "../common";

export interface ICustomer extends Auditable {
  customerId: string;
  customerName: string;
  customerType: CustomerType;
  phoneNumber: string;
  email?: string;
  active: boolean;
  address?: string;
  taxCode?: string;
  notes?: string;
}

export interface ICustomerInfo {
  customerId: string;
  customerName: string;
  customerType: CustomerType;
  phoneNumber: string;
  email?: string;
  active: boolean;
  address?: string;
  taxCode?: string;
  notes?: string;
}

export interface CustomerFilterCriteria {
  query?: string;
  active?: boolean;
  customerType?: string;
}
