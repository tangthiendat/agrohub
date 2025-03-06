import { PurchaseOrderStatus } from "../enums";

export const PURCHASE_ORDER_STATUS_NAME: Record<string, string> = {
  [PurchaseOrderStatus.PENDING]: "Chờ xác nhận",
  [PurchaseOrderStatus.APPROVED]: "Đã xác nhận",
  [PurchaseOrderStatus.COMPLETED]: "Hoàn thành",
  [PurchaseOrderStatus.CANCELLED]: "Đã hủy",
};
