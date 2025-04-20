import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  CreateExportInvoiceRequest,
  ExportInvoiceFilterCriteria,
  IExportInvoice,
  IStatsCardValue,
  Page,
  PaginationParams,
  SortParams,
} from "../../interfaces";

interface IExportInvoiceService {
  create(
    newExportInvoice: CreateExportInvoiceRequest,
  ): Promise<ApiResponse<void>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ExportInvoiceFilterCriteria,
  ): Promise<ApiResponse<Page<IExportInvoice>>>;
  getExportStatsCard(): Promise<ApiResponse<IStatsCardValue>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/export-invoices", {
  auth: true,
});

class ExportInvoiceService implements IExportInvoiceService {
  async create(
    newExportInvoice: CreateExportInvoiceRequest,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newExportInvoice)).data;
  }

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ExportInvoiceFilterCriteria,
  ): Promise<ApiResponse<Page<IExportInvoice>>> {
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

  async getExportStatsCard(): Promise<ApiResponse<IStatsCardValue>> {
    return (await apiClient.get("/stats/card")).data;
  }
}
export const exportInvoiceService = new ExportInvoiceService();
