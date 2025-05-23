import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  IUser,
  IUserInfo,
  Page,
  PaginationParams,
  SortParams,
  UserFilterCriteria,
} from "../../interfaces";

interface IUserService {
  getMe(): Promise<ApiResponse<IUser>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: UserFilterCriteria,
  ): Promise<ApiResponse<Page<IUser>>>;
  create(newUser: Omit<IUser, "userId">): Promise<ApiResponse<void>>;
  update(userId: string, updatedUser: IUser): Promise<ApiResponse<void>>;
  updateStatus(userId: string, active: boolean): Promise<ApiResponse<void>>;
  getUserInfo(): Promise<ApiResponse<IUserInfo>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/users", {
  auth: true,
});

class UserService implements IUserService {
  async getMe(): Promise<ApiResponse<IUser>> {
    return (await apiClient.get("/me")).data;
  }

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: UserFilterCriteria,
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

  async create(newUser: Omit<IUser, "userId">): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newUser)).data;
  }

  async update(userId: string, updatedUser: IUser): Promise<ApiResponse<void>> {
    return (await apiClient.put(`/${userId}`, updatedUser)).data;
  }

  async updateStatus(
    userId: string,
    active: boolean,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.patch(`/${userId}/status`, { active })).data;
  }

  async getUserInfo(): Promise<ApiResponse<IUserInfo>> {
    return (await apiClient.get("/info")).data;
  }
}

export const userService = new UserService();
