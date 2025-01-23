import { AxiosInstance } from "axios";
import {
  ApiResponse,
  IPermission,
  Page,
  PaginationParams,
  PermissionFilterCriteria,
  SortParams,
} from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IPermissionService {
  getPage(
    pagination: PaginationParams,
    filter?: PermissionFilterCriteria,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<IPermission>>>;
  create(
    newPermission: Omit<IPermission, "permissionId">,
  ): Promise<ApiResponse<void>>;
  update(
    permissionId: number,
    updatedPermission: IPermission,
  ): Promise<ApiResponse<void>>;
  delete(permissionId: number): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient(
  "auth-service/api/v1/permissions",
  {
    auth: true,
  },
);

class PermissionService implements IPermissionService {
  async getPage(
    pagination: PaginationParams,
    filter?: PermissionFilterCriteria,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<IPermission>>> {
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

  async create(
    newPermission: Omit<IPermission, "permissionId">,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newPermission)).data;
  }

  async update(
    permissionId: number,
    updatedPermission: IPermission,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.put(`/${permissionId}`, updatedPermission)).data;
  }

  async delete(permissionId: number): Promise<ApiResponse<void>> {
    return (await apiClient.delete(`/${permissionId}`)).data;
  }
}

export const permissionService = new PermissionService();
