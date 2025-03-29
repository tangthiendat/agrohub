import { DebtStatus, LocationStatus, PurchaseOrderStatus } from "../enums";

export * from "./notification";
export * from "./permission";
export * from "./user";
export * from "./status";
export * from "./product";
export * from "./location";

export const VIETNAM_TIMEZONE = "Asia/Ho_Chi_Minh";

export const COLOR_PRIMARY = "#4CAF50";
export const COLOR_SECONDARY = "#388E3C";
export const COLOR_ACCENT = "#E8F5E9";

export const PURCHASE_ORDER_STATUS_COLOR: Record<string, string> = {
  [PurchaseOrderStatus.PENDING]: "#FFC107",
  [PurchaseOrderStatus.APPROVED]: "#4CAF50",
  [PurchaseOrderStatus.COMPLETED]: "#2196F3",
  [PurchaseOrderStatus.CANCELLED]: "#F44336",
};

export const LOCATION_STATUS_COLOR: Record<string, string> = {
  [LocationStatus.AVAILABLE]: "green",
  [LocationStatus.OCCUPIED]: "blue",
  [LocationStatus.LOCKED]: "red",
  [LocationStatus.UNDER_MAINTENANCE]: "yellow",
};

export const DEBT_STATUS_COLOR: Record<string, string> = {
  [DebtStatus.UNPAID]: "red",
  [DebtStatus.PAID]: "green",
  [DebtStatus.OVERDUE]: "orange",
  [DebtStatus.PARTIALLY_PAID]: "blue",
};
