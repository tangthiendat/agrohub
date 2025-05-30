export enum ErrorType {
  VALIDATION = "VALIDATION",
  AUTHENTICATION = "AUTHENTICATION",
  RESOURCE = "RESOURCE",
  BUSINESS = "BUSINESS",
  SYSTEM = "SYSTEM",
}

export enum HttpMethod {
  GET = "GET",
  POST = "POST",
  PUT = "PUT",
  DELETE = "DELETE",
  PATCH = "PATCH",
}

export enum Module {
  USER = "USER",
  ROLE = "ROLE",
  PERMISSION = "PERMISSION",
  CATEGORY = "CATEGORY",
  UNIT = "UNIT",
  PRODUCT = "PRODUCT",
  SUPPLIER = "SUPPLIER",
  WAREHOUSE = "WAREHOUSE",
  PURCHASE_ORDER = "PURCHASE ORDER",
  IMPORT_INVOICE = "IMPORT INVOICE",
  PRODUCT_LOCATION = "PRODUCT LOCATION",
  PRODUCT_BATCH = "PRODUCT BATCH",
  PRODUCT_STOCK = "PRODUCT STOCK",
  DEBT_ACCOUNT = "DEBT ACCOUNT",
  PAYMENT = "PAYMENT",
  CUSTOMER = "CUSTOMER",
  EXPORT_INVOICE = "EXPORT INVOICE",
  RECEIPT = "RECEIPT",
}

export enum Gender {
  MALE = "MALE",
  FEMALE = "FEMALE",
  OTHER = "OTHER",
}

export enum PhysicalState {
  SOLID = "SOLID",
  LIQUID = "LIQUID",
  POWDER = "POWDER",
}

export enum PurchaseOrderStatus {
  PENDING = "PENDING",
  APPROVED = "APPROVED",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED",
}

export enum DiscountType {
  PERCENTAGE = "PERCENTAGE",
  AMOUNT = "AMOUNT",
}

export enum RackType {
  STACKING_RACK = "STACKING_RACK",
  PALLET_RACK = "PALLET_RACK",
}

export enum LocationStatus {
  AVAILABLE = "AVAILABLE",
  OCCUPIED = "OCCUPIED",
  LOCKED = "LOCKED",
  UNDER_MAINTENANCE = "UNDER_MAINTENANCE",
  OUT_OF_SERVICE = "OUT_OF_SERVICE",
}

export enum DebtSourceType {
  IMPORT_INVOICE = "IMPORT_INVOICE",
  EXPORT_INVOICE = "EXPORT_INVOICE",
  SALES_ORDER = "SALES_ORDER",
  ADJUSTMENT = "ADJUSTMENT",
}

export enum DebtStatus {
  UNPAID = "UNPAID",
  PAID = "PAID",
  OVERDUE = "OVERDUE",
  PARTIALLY_PAID = "PARTIALLY_PAID",
}

export enum DebtTransactionType {
  PAYMENT = "PAYMENT",
  DEBT = "DEBT",
  ADJUSTMENT = "ADJUSTMENT",
  INTEREST = "INTEREST",
}

export enum CustomerType {
  INDIVIDUAL = "INDIVIDUAL",
  BUSINESS = "BUSINESS",
}

export enum TrendType {
  UP = "UP",
  DOWN = "DOWN",
  STABLE = "STABLE",
}
