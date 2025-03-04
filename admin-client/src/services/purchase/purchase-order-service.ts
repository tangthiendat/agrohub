import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  CreatePurchaseOrderRequest,
  IPurchaseOrderTableItem,
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
  ): Promise<ApiResponse<Page<IPurchaseOrderTableItem>>>;
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
  ): Promise<ApiResponse<Page<IPurchaseOrderTableItem>>> {
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
}

export const purchaseOrderService = new PurchaseOrderService();
