import { DebtSourceType } from "../../common/enums";

export interface IPaymentMethod {
  paymentMethodId: string;
  paymentMethodName: string;
  description?: string;
}

export interface IPartyDebtAccount {
  debtAccountId: string;
  sourceId: string;
  sourceType: DebtSourceType;
  totalAmount: number;
  paidAmount: number;
  remainingAmount: number;
  interestRate: number;
  dueDate: string;
}

export interface CreatePaymentDetailRequest {
  debtAccountId: string;
  paymentAmount: number;
}

export interface CreatePaymentRequest {
  supplierId: string;
  warehouseId: number;
  userId: string;
  createdDate: string;
  totalPaidAmount: number;
  paymentMethodId: string;
  paymentDetails: CreatePaymentDetailRequest[];
}
