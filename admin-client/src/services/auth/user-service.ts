import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, IUser } from "../../interfaces";

interface IUserService {
  getMe(): Promise<ApiResponse<IUser>>;
}

const apiClient: AxiosInstance = createApiClient("auth-service/api/v1/users", {
  auth: true,
});

class UserService implements IUserService {
  async getMe(): Promise<ApiResponse<IUser>> {
    return (await apiClient.get("/me")).data;
  }
}

export const userService = new UserService();
