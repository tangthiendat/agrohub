import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  CreateImportInvoiceRequest,
  IImportInvoice,
  ImportInvoiceFilterCriteria,
  Page,
  PaginationParams,
  SortParams,
} from "../../interfaces";

interface IImportInvoiceService {
  create(
    newImportInvoice: CreateImportInvoiceRequest,
  ): Promise<ApiResponse<void>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ImportInvoiceFilterCriteria,
  ): Promise<ApiResponse<Page<IImportInvoice>>>;
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

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: ImportInvoiceFilterCriteria,
  ): Promise<ApiResponse<Page<IImportInvoice>>> {
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
}

export const importInvoiceService: IImportInvoiceService =
  new ImportInvoiceService();
