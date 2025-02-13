import { AxiosInstance } from "axios";
import { ApiResponse } from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IProductService {
  create(newProduct: FormData): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/products", {
  auth: true,
});

class ProductService implements IProductService {
  async create(newProduct: FormData): Promise<ApiResponse<void>> {
    return (
      await apiClient.post("", newProduct, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
    ).data;
  }
}

export const productService = new ProductService();
