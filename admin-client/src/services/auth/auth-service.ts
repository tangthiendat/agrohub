import { AxiosInstance } from "axios";
import { ApiResponse, IAuthRequest, IAuthResponse } from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface IAuthService {
  login(authRequest: IAuthRequest): Promise<ApiResponse<IAuthResponse>>;
  logout(): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/auth", {
  auth: false,
});
class AuthService implements IAuthService {
  async login(authRequest: IAuthRequest): Promise<ApiResponse<IAuthResponse>> {
    return (await apiClient.post("/login", authRequest)).data;
  }

  async logout(): Promise<ApiResponse<void>> {
    const logoutApiClient: AxiosInstance = createApiClient("api/v1/auth", {
      auth: true,
    });
    return (await logoutApiClient.post("/logout")).data;
  }
}

export const authService = new AuthService();
