import { AxiosInstance } from "axios";
import {
  ApiResponse,
  IRole,
  Page,
  PaginationParams,
  SortParams,
} from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IRoleService {
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<IRole>>>;
}

const apiClient: AxiosInstance = createApiClient("auth-service/api/v1/roles", {
  auth: true,
});

class RoleService implements IRoleService {
  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<IRole>>> {
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

export const roleService = new RoleService();
