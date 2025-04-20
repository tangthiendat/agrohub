import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ActivityChartData,
  ApiResponse,
  IStatsCardValue,
  StatisticFilterCriteria,
} from "../../interfaces";

interface IInvoiceService {
  getOrderStatsCard(): Promise<ApiResponse<IStatsCardValue>>;
  getActivityStats(
    filter: StatisticFilterCriteria,
  ): Promise<ApiResponse<ActivityChartData[]>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/invoices", {
  auth: true,
});

class InvoiceService implements IInvoiceService {
  async getOrderStatsCard(): Promise<ApiResponse<IStatsCardValue>> {
    return (await apiClient.get("/stats/card")).data;
  }

  async getActivityStats(
    filter: StatisticFilterCriteria,
  ): Promise<ApiResponse<ActivityChartData[]>> {
    return (
      await apiClient.get("/stats/activity", {
        params: filter,
      })
    ).data;
  }
}

export const invoiceService = new InvoiceService();
