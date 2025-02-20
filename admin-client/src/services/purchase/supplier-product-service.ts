import { AxiosInstance } from "axios";
import { ApiResponse, ISupplier, ISupplierProduct } from "../../interfaces";
import { createApiClient } from "../../config/axios/api-client";

interface ISupplierProductService {
  create(
    supplierProduct: Omit<ISupplierProduct, "supplierProductId">,
  ): Promise<ApiResponse<void>>;
  getSupplierByProductId(productId: string): Promise<ApiResponse<ISupplier[]>>;
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

  async getSupplierByProductId(
    productId: string,
  ): Promise<ApiResponse<ISupplier[]>> {
    return (await apiClient.get(`product/${productId}`)).data;
  }
}

export const supplierProductService = new SupplierProductService();
