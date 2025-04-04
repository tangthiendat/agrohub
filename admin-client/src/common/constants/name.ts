import {
  CustomerType,
  DebtTransactionType,
  Gender,
  PhysicalState,
  RackType,
} from "../enums";

export const GENDER_NAME: Record<string, string> = {
  [Gender.MALE]: "Nam",
  [Gender.FEMALE]: "Nữ",
  [Gender.OTHER]: "Khác",
};

export const RACK_TYPE_NAME: Record<string, string> = {
  [RackType.STACKING_RACK]: "Kệ xếp chồng",
  [RackType.PALLET_RACK]: "Kệ để pallet",
};

export const PHYSICAL_STATE_NAME: Record<string, string> = {
  [PhysicalState.SOLID]: "Rắn",
  [PhysicalState.LIQUID]: "Lỏng",
  [PhysicalState.POWDER]: "Bột",
};

export const CUSTOMER_TYPE_NAME: Record<string, string> = {
  [CustomerType.INDIVIDUAL]: "Cá nhân",
  [CustomerType.BUSINESS]: "Doanh nghiệp",
};

export const DEBT_TRANSACTION_TYPE_NAME: Record<string, string> = {
  [DebtTransactionType.DEBT]: "Ghi nợ",
  [DebtTransactionType.PAYMENT]: "Thanh toán nợ",
  [DebtTransactionType.INTEREST]: "Lãi suất",
  [DebtTransactionType.ADJUSTMENT]: "Điều chỉnh",
};
