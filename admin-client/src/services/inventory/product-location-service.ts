import { AxiosInstance } from "axios";
import { ApiResponse, IProductLocation } from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IProductLocationService {
  create: (productLocation: IProductLocation) => Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/product-locations", {
  auth: true,
});

class ProductLocationService implements IProductLocationService {
  async create(productLocation: IProductLocation): Promise<ApiResponse<void>> {
    return (await apiClient.post("", productLocation)).data;
  }
}

export const productLocationService = new ProductLocationService();
