import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  ICategory,
  Page,
  PaginationParams,
  SortParams,
} from "../../interfaces";

interface ICategoryService {
  create(
    newCategory: Omit<ICategory, "categoryId">,
  ): Promise<ApiResponse<void>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<ICategory>>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/categories", {
  auth: true,
});

class CategoryService implements ICategoryService {
  async create(
    newCategory: Omit<ICategory, "categoryId">,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newCategory)).data;
  }

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<ICategory>>> {
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

export const categoryService = new CategoryService();
