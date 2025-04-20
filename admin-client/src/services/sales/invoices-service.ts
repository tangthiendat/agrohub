import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, IStatsCardValue } from "../../interfaces";

interface IInvoiceService {
  getOrderStatsCard(): Promise<ApiResponse<IStatsCardValue>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/invoices", {
  auth: true,
});

class InvoiceService implements IInvoiceService {
  async getOrderStatsCard(): Promise<ApiResponse<IStatsCardValue>> {
    return (await apiClient.get("/stats/card")).data;
  }
}

export const invoiceService = new InvoiceService();
