import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  IUser,
  Page,
  PaginationParams,
  PermissionFilterCriteria,
  SortParams,
} from "../../interfaces";

interface IUserService {
  getMe(): Promise<ApiResponse<IUser>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PermissionFilterCriteria,
  ): Promise<ApiResponse<Page<IUser>>>;
}

const apiClient: AxiosInstance = createApiClient("auth-service/api/v1/users", {
  auth: true,
});

class UserService implements IUserService {
  async getMe(): Promise<ApiResponse<IUser>> {
    return (await apiClient.get("/me")).data;
  }

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PermissionFilterCriteria,
  ): Promise<ApiResponse<Page<IUser>>> {
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
}

export const userService = new UserService();
