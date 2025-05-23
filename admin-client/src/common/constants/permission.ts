import { HttpMethod, Module } from "../enums";

export const PERMISSIONS = {
  [Module.PERMISSION]: {
    GET_PAGE: {
      apiPath: "/api/v1/permissions/page",
      httpMethod: HttpMethod.GET,
    },
    GET_ALL: {
      apiPath: "/api/v1/permissions",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/permissions",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/permissions/{id}",
      httpMethod: HttpMethod.PUT,
    },
    UPDATE_STATUS: {
      apiPath: "/api/v1/permissions/{id}/status",
      httpMethod: HttpMethod.PATCH,
    },
    DELETE: {
      apiPath: "/api/v1/permissions/{id}",
      httpMethod: HttpMethod.DELETE,
    },
  },
  [Module.ROLE]: {
    GET_PAGE: {
      apiPath: "/api/v1/roles/page",
      httpMethod: HttpMethod.GET,
    },
    GET_ALL: {
      apiPath: "/api/v1/roles",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/roles",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/roles/{id}",
      httpMethod: HttpMethod.PUT,
    },
    UPDATE_STATUS: {
      apiPath: "/api/v1/roles/{id}/status",
      httpMethod: HttpMethod.PATCH,
    },
  },
  [Module.USER]: {
    GET_PAGE: {
      apiPath: "/api/v1/users/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/users",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/users/{id}",
      httpMethod: HttpMethod.PUT,
    },
    UPDATE_STATUS: {
      apiPath: "/api/v1/users/{id}/status",
      httpMethod: HttpMethod.PATCH,
    },
  },
  [Module.CATEGORY]: {
    GET_PAGE: {
      apiPath: "/api/v1/categories/page",
      httpMethod: HttpMethod.GET,
    },
    GET_ALL: {
      apiPath: "/api/v1/categories",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/categories",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/categories/{id}",
      httpMethod: HttpMethod.PUT,
    },
  },
  [Module.UNIT]: {
    GET_PAGE: {
      apiPath: "/api/v1/units/page",
      httpMethod: HttpMethod.GET,
    },
    GET_ALL: {
      apiPath: "/api/v1/units",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/units",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/units/{id}",
      httpMethod: HttpMethod.PUT,
    },
  },
  [Module.PRODUCT]: {
    GET_PAGE: {
      apiPath: "/api/v1/products/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/products",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/products/{id}",
      httpMethod: HttpMethod.PUT,
    },
  },
  [Module.SUPPLIER]: {
    GET_PAGE: {
      apiPath: "/api/v1/suppliers/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/suppliers",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/suppliers/{id}",
      httpMethod: HttpMethod.PUT,
    },
    UPDATE_STATUS: {
      apiPath: "/api/v1/suppliers/{id}/status",
      httpMethod: HttpMethod.PATCH,
    },
    RATE: {
      apiPath: "/api/v1/suppliers/{id}/ratings",
      httpMethod: HttpMethod.POST,
    },
  },
  [Module.WAREHOUSE]: {
    GET_PAGE: {
      apiPath: "/api/v1/warehouses/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/warehouses",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/warehouses/{id}",
      httpMethod: HttpMethod.PUT,
    },
  },
  [Module.PURCHASE_ORDER]: {
    GET_PAGE: {
      apiPath: "/api/v1/purchase-orders/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/purchase-orders",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/purchase-orders/{id}",
      httpMethod: HttpMethod.PUT,
    },
    UPDATE_STATUS: {
      apiPath: "/api/v1/purchase-orders/{id}/status",
      httpMethod: HttpMethod.PATCH,
    },
  },
  [Module.IMPORT_INVOICE]: {
    GET_PAGE: {
      apiPath: "/api/v1/import-invoices/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/import-invoices",
      httpMethod: HttpMethod.POST,
    },
  },
  [Module.PRODUCT_LOCATION]: {
    GET_PAGE: {
      apiPath: "/api/v1/product-locations/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/product-locations",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/product-locations/{id}",
      httpMethod: HttpMethod.PUT,
    },
  },
  [Module.PRODUCT_BATCH]: {
    GET_PAGE: {
      apiPath: "/api/v1/product-batches/page",
      httpMethod: HttpMethod.GET,
    },
    UPDATE: {
      apiPath: "/api/v1/product-batches/{id}",
      httpMethod: HttpMethod.PATCH,
    },
  },
  [Module.PRODUCT_STOCK]: {
    GET_PAGE: {
      apiPath: "/api/v1/product-stocks/page",
      httpMethod: HttpMethod.GET,
    },
  },
  [Module.DEBT_ACCOUNT]: {
    GET_SUPPLIER_UNPAID: {
      apiPath: "/api/v1/debt-accounts/supplier/{supplierId}/unpaid",
      httpMethod: HttpMethod.GET,
    },
    GET_SUPPLIER_PAGE: {
      apiPath: "/api/v1/debt-accounts/supplier/{supplierId}/page",
      httpMethod: HttpMethod.GET,
    },
  },
  [Module.PAYMENT]: {
    GET_PAGE: {
      apiPath: "/api/v1/payments/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/payments",
      httpMethod: HttpMethod.POST,
    },
  },
  [Module.CUSTOMER]: {
    GET_PAGE: {
      apiPath: "/api/v1/customers/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/customers",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/api/v1/customers/{id}",
      httpMethod: HttpMethod.PUT,
    },
    UPDATE_STATUS: {
      apiPath: "/api/v1/customers/{id}/status",
      httpMethod: HttpMethod.PATCH,
    },
  },
  [Module.EXPORT_INVOICE]: {
    GET_PAGE: {
      apiPath: "/api/v1/export-invoices/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/api/v1/export-invoices",
      httpMethod: HttpMethod.POST,
    },
  },
  [Module.RECEIPT]: {
    CREATE: {
      apiPath: "/api/v1/receipts",
      httpMethod: HttpMethod.POST,
    },
  },
};
