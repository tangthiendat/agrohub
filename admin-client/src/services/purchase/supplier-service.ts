import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  ISupplier,
  ISupplierRating,
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
  update(
    supplierId: string,
    updatedSupplier: ISupplier,
  ): Promise<ApiResponse<void>>;
  updateStatus(supplierId: string, active: boolean): Promise<ApiResponse<void>>;
  search(query?: string): Promise<ApiResponse<ISupplier[]>>;
  createRating(
    supplierId: string,
    supplierRating: ISupplierRating,
  ): Promise<ApiResponse<void>>;
  updateRating(
    supplierId: string,
    ratingId: string,
    supplierRating: ISupplierRating,
  ): Promise<ApiResponse<void>>;
  getById(supplierId: string): Promise<ApiResponse<ISupplier>>;
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

  async update(
    supplierId: string,
    updatedSupplier: ISupplier,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.put(`/${supplierId}`, updatedSupplier)).data;
  }

  async updateStatus(
    supplierId: string,
    active: boolean,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.patch(`/${supplierId}/status`, { active })).data;
  }

  async search(query?: string): Promise<ApiResponse<ISupplier[]>> {
    return (await apiClient.get("/search", { params: { query } })).data;
  }

  async createRating(
    supplierId: string,
    supplierRating: ISupplierRating,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.post(`/${supplierId}/ratings`, supplierRating))
      .data;
  }

  async updateRating(
    supplierId: string,
    ratingId: string,
    supplierRating: ISupplierRating,
  ): Promise<ApiResponse<void>> {
    return (
      await apiClient.patch(
        `/${supplierId}/ratings/${ratingId}`,
        supplierRating,
      )
    ).data;
  }

  async getById(supplierId: string): Promise<ApiResponse<ISupplier>> {
    return (await apiClient.get(`/${supplierId}`)).data;
  }
}

export const supplierService = new SupplierService();
