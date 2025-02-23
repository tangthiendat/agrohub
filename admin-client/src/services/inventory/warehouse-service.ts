import { AxiosInstance } from "axios";
import {
  ApiResponse,
  IWarehouse,
  Page,
  PaginationParams,
  SortParams,
} from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IWarehouseService {
  create(
    warehouse: Omit<IWarehouse, "warehouseId">,
  ): Promise<ApiResponse<void>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<IWarehouse>>>;
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

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<IWarehouse>>> {
    return (
      await apiClient.get("/page", {
        params: {
          ...pagination,
          sortBy: sort?.sortBy !== "" ? sort?.sortBy : undefined,
          direction: sort?.direction !== "" ? sort?.direction : undefined,
        },
      })
    ).data;
  }
}

export const warehouseService = new WarehouseService();
