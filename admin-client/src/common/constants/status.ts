import { LocationStatus, PurchaseOrderStatus } from "../enums";

export const PURCHASE_ORDER_STATUS_NAME: Record<string, string> = {
  [PurchaseOrderStatus.PENDING]: "Chờ xác nhận",
  [PurchaseOrderStatus.APPROVED]: "Đã xác nhận",
  [PurchaseOrderStatus.COMPLETED]: "Hoàn thành",
  [PurchaseOrderStatus.CANCELLED]: "Đã hủy",
};

export const LOCATION_STATUS_NAME: Record<string, string> = {
  [LocationStatus.AVAILABLE]: "Sẵn sàng",
  [LocationStatus.OCCUPIED]: "Đã sử dụng",
  [LocationStatus.LOCKED]: "Đã khóa",
  [LocationStatus.UNDER_MAINTENANCE]: "Đang bảo trì",
  [LocationStatus.OUT_OF_SERVICE]: "Ngừng sử dụng",
};

export const DEBT_STATUS_NAME: Record<string, string> = {
  UNPAID: "Chưa thanh toán",
  PAID: "Đã thanh toán",
  OVERDUE: "Quá hạn",
  PARTIALLY_PAID: "Thanh toán một phần",
};
