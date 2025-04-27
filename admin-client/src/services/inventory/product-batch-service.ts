import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  IProductBatch,
  Page,
  PaginationParams,
  ProductBatchFilterCriteria,
  SortParams,
} from "../../interfaces";

interface IProductBatchService {
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ProductBatchFilterCriteria,
  ): Promise<ApiResponse<Page<IProductBatch>>>;
  update(
    batchId: string,
    updatedProductBatch: IProductBatch,
  ): Promise<ApiResponse<void>>;
  getByProductId(productId: string): Promise<ApiResponse<IProductBatch[]>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/product-batches", {
  auth: true,
});

class ProductBatchService implements IProductBatchService {
  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ProductBatchFilterCriteria,
  ): Promise<ApiResponse<Page<IProductBatch>>> {
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
    batchId: string,
    updatedProductBatch: IProductBatch,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.patch(`/${batchId}`, updatedProductBatch)).data;
  }

  async getByProductId(
    productId: string,
  ): Promise<ApiResponse<IProductBatch[]>> {
    return (
      await apiClient.get("", {
        params: { productId },
      })
    ).data;
  }
}

export const productBatchService = new ProductBatchService();
