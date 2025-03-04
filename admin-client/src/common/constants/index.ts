import { PurchaseOrderStatus } from "../enums";

export * from "./notification";
export * from "./permission";
export * from "./user";

export const VIETNAM_TIMEZONE = "Asia/Ho_Chi_Minh";

export const COLOR_PRIMARY = "#4CAF50";
export const COLOR_SECONDARY = "#388E3C";
export const COLOR_ACCENT = "#E8F5E9";

export const PURCHASE_ORDER_STATUS_COLOR = {
  [PurchaseOrderStatus.PENDING]: "#FFC107",
  [PurchaseOrderStatus.APPROVED]: "#4CAF50",
  [PurchaseOrderStatus.COMPLETED]: "#2196F3",
  [PurchaseOrderStatus.CANCELED]: "#F44336",
};
