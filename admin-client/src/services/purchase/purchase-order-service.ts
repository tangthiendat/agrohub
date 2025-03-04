import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  CreatePurchaseOrderRequest,
  IPurchaseOrderListItem,
  Page,
  PaginationParams,
  PurchaseOrderFilterCriteria,
  SortParams,
} from "../../interfaces";

interface IPurchaseOrderService {
  create(
    newPurchaseOrder: CreatePurchaseOrderRequest,
  ): Promise<ApiResponse<void>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PurchaseOrderFilterCriteria,
  ): Promise<ApiResponse<Page<IPurchaseOrderListItem>>>;
  get(
    filter: PurchaseOrderFilterCriteria,
  ): Promise<ApiResponse<IPurchaseOrderListItem[]>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/purchase-orders", {
  auth: true,
});

class PurchaseOrderService implements IPurchaseOrderService {
  async create(
    newPurchaseOrder: CreatePurchaseOrderRequest,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newPurchaseOrder)).data;
  }

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PurchaseOrderFilterCriteria,
  ): Promise<ApiResponse<Page<IPurchaseOrderListItem>>> {
    return (
      await apiClient.get("/page", {
        params: {
          ...pagination,
          ...filter,
          sortBy: sort?.sortBy !== "" ? sort?.sortBy : undefined,
          direction: sort?.direction !== "" ? sort?.direction : undefined,
        },
      })
    ).data;
  }

  async get(
    filter: PurchaseOrderFilterCriteria,
  ): Promise<ApiResponse<IPurchaseOrderListItem[]>> {
    return (await apiClient.get("", { params: filter })).data;
  }
}

export const purchaseOrderService = new PurchaseOrderService();
