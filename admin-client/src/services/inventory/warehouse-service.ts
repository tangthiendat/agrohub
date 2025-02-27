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
  update(
    warehouseId: number,
    updatedWarehouse: IWarehouse,
  ): Promise<ApiResponse<void>>;
  getAll(): Promise<ApiResponse<IWarehouse[]>>;
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

  async update(
    warehouseId: number,
    updatedWarehouse: IWarehouse,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.put(`/${warehouseId}`, updatedWarehouse)).data;
  }

  async getAll(): Promise<ApiResponse<IWarehouse[]>> {
    return (await apiClient.get("")).data;
  }
}

export const warehouseService = new WarehouseService();
