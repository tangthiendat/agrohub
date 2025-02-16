import { AxiosInstance } from "axios";
import {
  ApiResponse,
  IProduct,
  Page,
  PaginationParams,
  ProductFilterCriteria,
  SortParams,
} from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IProductService {
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ProductFilterCriteria,
  ): Promise<ApiResponse<Page<IProduct>>>;
  getById(productId: string): Promise<ApiResponse<IProduct>>;
  create(newProduct: FormData): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/products", {
  auth: true,
});

class ProductService implements IProductService {
  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ProductFilterCriteria,
  ): Promise<ApiResponse<Page<IProduct>>> {
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

  async getById(productId: string): Promise<ApiResponse<IProduct>> {
    return (await apiClient.get(`/${productId}`)).data;
  }

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
