import { AxiosInstance } from "axios";
import { ApiResponse, CreateProductLocationRequest } from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IProductLocationService {
  create: (
    productLocation: CreateProductLocationRequest,
  ) => Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/product-locations", {
  auth: true,
});

class ProductLocationService implements IProductLocationService {
  async create(
    productLocation: CreateProductLocationRequest,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post("", productLocation)).data;
  }
}

export const productLocationService = new ProductLocationService();
