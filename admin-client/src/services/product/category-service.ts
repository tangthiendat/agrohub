import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, ICategory } from "../../interfaces";

interface ICategoryService {
  create(
    newCategory: Omit<ICategory, "categoryId">,
  ): Promise<ApiResponse<void>>;
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
}

export const categoryService = new CategoryService();
