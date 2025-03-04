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
  PERCENT = "PERCENT",
  AMOUNT = "AMOUNT",
}
