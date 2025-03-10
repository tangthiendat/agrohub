import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, CreateImportInvoiceRequest } from "../../interfaces";

interface IImportInvoiceService {
  create: (
    newImportInvoice: CreateImportInvoiceRequest,
  ) => Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/import-invoices", {
  auth: true,
});

class ImportInvoiceService implements IImportInvoiceService {
  async create(
    newImportInvoice: CreateImportInvoiceRequest,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newImportInvoice)).data;
  }
}

export const importInvoiceService: IImportInvoiceService =
  new ImportInvoiceService();
