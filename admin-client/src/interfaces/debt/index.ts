import {
  DebtSourceType,
  DebtStatus,
  DebtTransactionType,
} from "../../common/enums";

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
  debtTransactions: IDebtTransaction[];
}

export interface IDebtTransaction {
  debtTransactionId: string;
  amount: number;
  sourceId: string;
  transactionType: DebtTransactionType;
  createdAt: string;
}

export interface IPartyDebtAccount {
  debtAccountId: string;
  partyId: string;
  sourceId: string;
  sourceType: DebtSourceType;
  totalAmount: number;
  paidAmount: number;
  remainingAmount: number;
  interestRate: number;
  dueDate: string;
  debtStatus: DebtStatus;
}

export interface PartyDebtAccountFilterCriteria {
  debtStatus?: string;
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

export interface CreateReceiptDetailRequest {
  debtAccountId: string;
  receiptAmount: number;
}

export interface CreateReceiptRequest {
  customerId: string;
  warehouseId: number;
  userId: string;
  createdDate: string;
  totalReceivedAmount: number;
  paymentMethodId: string;
  receiptDetails: CreateReceiptDetailRequest[];
}
