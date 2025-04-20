import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, CreateReceiptRequest } from "../../interfaces";

interface IReceiptService {
  create(receipt: CreateReceiptRequest): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/receipts", {
  auth: true,
});

class ReceiptService implements IReceiptService {
  async create(receipt: CreateReceiptRequest): Promise<ApiResponse<void>> {
    return (await apiClient.post("", receipt)).data;
  }
}

export const receiptService = new ReceiptService();
