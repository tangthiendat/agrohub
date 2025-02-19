import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, ISupplier } from "../../interfaces";

interface ISupplierService {
  create(
    newSupplier: Omit<ISupplier, "supplierId">,
  ): Promise<ApiResponse<void>>;
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
}

export const supplierService = new SupplierService();
