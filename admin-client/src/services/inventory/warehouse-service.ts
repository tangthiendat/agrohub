import { AxiosInstance } from "axios";
import { ApiResponse, IWarehouse } from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IWarehouseService {
  create(
    warehouse: Omit<IWarehouse, "warehouseId">,
  ): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/warehouses", {
  auth: true,
});

class WarehouseService implements IWarehouseService {
  async create(
    warehouse: Omit<IWarehouse, "warehouseId">,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post("", warehouse)).data;
  }
}

export const warehouseService = new WarehouseService();
