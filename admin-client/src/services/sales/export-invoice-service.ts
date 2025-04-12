import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, CreateExportInvoiceRequest } from "../../interfaces";

interface IExportInvoiceService {
  create(
    newExportInvoice: CreateExportInvoiceRequest,
  ): Promise<ApiResponse<void>>;
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
}
export const exportInvoiceService = new ExportInvoiceService();
