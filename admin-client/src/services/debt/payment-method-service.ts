import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, IPaymentMethod } from "../../interfaces";

interface IPaymentMethodService {
  getAll(): Promise<ApiResponse<IPaymentMethod[]>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/payment-methods", {
  auth: true,
});

class PaymentMethodService implements IPaymentMethodService {
  async getAll(): Promise<ApiResponse<IPaymentMethod[]>> {
    return (await apiClient.get("")).data;
  }
}

export const paymentMethodService = new PaymentMethodService();
