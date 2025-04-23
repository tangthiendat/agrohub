import { AxiosInstance } from "axios";
import {
  ApiResponse,
  CategoryInventoryData,
  IProductStock,
  Page,
  PaginationParams,
  ProductStockFilterCriteria,
  SortParams,
} from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IProductStockService {
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ProductStockFilterCriteria,
  ): Promise<ApiResponse<Page<IProductStock>>>;
  getCategoryInventoryStats(): Promise<ApiResponse<CategoryInventoryData[]>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/product-stocks", {
  auth: true,
});

class ProductStockService implements IProductStockService {
  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ProductStockFilterCriteria,
  ): Promise<ApiResponse<Page<IProductStock>>> {
    return (
      await apiClient.get("/page", {
        params: {
          ...pagination,
          ...filter,
          sortBy: sort?.sortBy || undefined,
          direction: sort?.direction || undefined,
        },
      })
    ).data;
  }

  async getCategoryInventoryStats(): Promise<
    ApiResponse<CategoryInventoryData[]>
  > {
    return (await apiClient.get("/stats/category-inventory")).data;
  }
}

export const productStockService = new ProductStockService();
