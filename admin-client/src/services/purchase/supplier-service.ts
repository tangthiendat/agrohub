import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  ISupplier,
  Page,
  PaginationParams,
  SortParams,
  SupplierFilterCriteria,
} from "../../interfaces";

interface ISupplierService {
  create(
    newSupplier: Omit<ISupplier, "supplierId">,
  ): Promise<ApiResponse<void>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: SupplierFilterCriteria,
  ): Promise<ApiResponse<Page<ISupplier>>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/suppliers", {
  auth: true,
});

class SupplierService implements ISupplierService {
  async create(
    newSupplier: Omit<ISupplier, "supplierId">,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newSupplier)).data;
  }

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: SupplierFilterCriteria,
  ): Promise<ApiResponse<Page<ISupplier>>> {
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

export const supplierService = new SupplierService();
