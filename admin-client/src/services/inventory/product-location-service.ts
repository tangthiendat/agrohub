import { AxiosInstance } from "axios";
import {
  ApiResponse,
  IProductLocation,
  Page,
  PaginationParams,
  ProductLocationFilterCriteria,
  SortParams,
} from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IProductLocationService {
  create(productLocation: IProductLocation): Promise<ApiResponse<void>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ProductLocationFilterCriteria,
  ): Promise<ApiResponse<Page<IProductLocation>>>;
  update(
    productLocationId: string,
    updatedProductLocation: IProductLocation,
  ): Promise<ApiResponse<void>>;
  search(query: string): Promise<ApiResponse<IProductLocation[]>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/product-locations", {
  auth: true,
});

class ProductLocationService implements IProductLocationService {
  async create(productLocation: IProductLocation): Promise<ApiResponse<void>> {
    return (await apiClient.post("", productLocation)).data;
  }

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ProductLocationFilterCriteria,
  ): Promise<ApiResponse<Page<IProductLocation>>> {
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

  async update(
    productLocationId: string,
    updatedProductLocation: IProductLocation,
  ): Promise<ApiResponse<void>> {
    return (
      await apiClient.put(`/${productLocationId}`, updatedProductLocation)
    ).data;
  }

  async search(query: string): Promise<ApiResponse<IProductLocation[]>> {
    return (
      await apiClient.get("/search", {
        params: { query },
      })
    ).data;
  }
}

export const productLocationService = new ProductLocationService();
