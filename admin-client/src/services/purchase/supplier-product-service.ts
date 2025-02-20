import { AxiosInstance } from "axios";
import { ApiResponse, ISupplierProduct } from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface ISupplierProductService {
  create(
    supplierProduct: Omit<ISupplierProduct, "supplierProductId">,
  ): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/supplier-products", {
  auth: true,
});

class SupplierProductService implements ISupplierProductService {
  async create(
    supplierProduct: Omit<ISupplierProduct, "supplierProductId">,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post("", supplierProduct)).data;
  }
}

export const supplierProductService = new SupplierProductService();
