import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  CreatePurchaseOrderRequest,
  IPurchaseOrder,
  IPurchaseOrderListItem,
  Page,
  PaginationParams,
  PurchaseOrderFilterCriteria,
  SortParams,
  UpdatePurchaseOrderRequest,
} from "../../interfaces";
import { PurchaseOrderStatus } from "../../common/enums";

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
  update(
    purchaseOrderId: string,
    purchaseOrder: UpdatePurchaseOrderRequest,
  ): Promise<ApiResponse<void>>;
  updateStatus(
    purchaseOrderId: string,
    status: PurchaseOrderStatus,
  ): Promise<ApiResponse<void>>;
  getById(purchaseOrderId: string): Promise<ApiResponse<IPurchaseOrder>>;
  cancel(
    purchaseOrderId: string,
    cancelReason: string,
  ): Promise<ApiResponse<void>>;
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

  async update(
    purchaseOrderId: string,
    purchaseOrder: UpdatePurchaseOrderRequest,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.put(`/${purchaseOrderId}`, purchaseOrder)).data;
  }

  async updateStatus(
    purchaseOrderId: string,
    status: PurchaseOrderStatus,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.patch(`/${purchaseOrderId}/status`, { status }))
      .data;
  }

  async getById(purchaseOrderId: string): Promise<ApiResponse<IPurchaseOrder>> {
    return (await apiClient.get(`/${purchaseOrderId}`)).data;
  }

  async cancel(
    purchaseOrderId: string,
    cancelReason: string,
  ): Promise<ApiResponse<void>> {
    return (
      await apiClient.patch(`/${purchaseOrderId}/cancel`, { cancelReason })
    ).data;
  }
}

export const purchaseOrderService = new PurchaseOrderService();
