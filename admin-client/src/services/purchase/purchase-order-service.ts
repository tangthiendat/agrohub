import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, CreatePurchaseOrderRequest } from "../../interfaces";

interface IPurchaseOrderService {
  create(
    newPurchaseOrder: CreatePurchaseOrderRequest,
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
}

export const purchaseOrderService = new PurchaseOrderService();
