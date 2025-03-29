import { DebtTransactionType } from "../enums";

export const DEBT_TRANSACTION_TYPE_NAME: Record<string, string> = {
  [DebtTransactionType.DEBT]: "Ghi nợ",
  [DebtTransactionType.PAYMENT]: "Thanh toán nợ",
  [DebtTransactionType.INTEREST]: "Lãi suất",
  [DebtTransactionType.ADJUSTMENT]: "Điều chỉnh",
};
