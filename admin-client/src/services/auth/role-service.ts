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
  getAll(): Promise<ApiResponse<Omit<IRole, "permissions">[]>>;
  create(newRole: Omit<IRole, "roleId">): Promise<ApiResponse<void>>;
  update(roleId: number, updatedRole: IRole): Promise<ApiResponse<void>>;
  updateStatus(roleId: number, active: boolean): Promise<ApiResponse<void>>;
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

  async getAll(): Promise<ApiResponse<Omit<IRole, "permissions">[]>> {
    return (await apiClient.get("")).data;
  }

  async create(newRole: Omit<IRole, "roleId">): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newRole)).data;
  }

  async update(roleId: number, updatedRole: IRole): Promise<ApiResponse<void>> {
    return (await apiClient.put(`/${roleId}`, updatedRole)).data;
  }

  async updateStatus(
    roleId: number,
    active: boolean,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.patch(`/${roleId}/status`, { active })).data;
  }
}

export const roleService = new RoleService();
