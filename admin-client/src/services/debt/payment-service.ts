import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, CreatePaymentRequest } from "../../interfaces";

interface IPaymentService {
  create(payment: CreatePaymentRequest): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/payments", {
  auth: true,
});

class PaymentService implements IPaymentService {
  async create(payment: CreatePaymentRequest): Promise<ApiResponse<void>> {
    return (await apiClient.post("", payment)).data;
  }
}

export const paymentService = new PaymentService();
