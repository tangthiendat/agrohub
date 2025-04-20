import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  IPartyDebtAccount,
  Page,
  PaginationParams,
  PartyDebtAccountFilterCriteria,
  SortParams,
} from "../../interfaces";

export interface IDebtAccountService {
  getUnpaidSupplierDebtAccount(
    supplierId: string,
  ): Promise<ApiResponse<IPartyDebtAccount[]>>;
  getUnpaidCustomerDebtAccount(
    customerId: string,
  ): Promise<ApiResponse<IPartyDebtAccount[]>>;
  getSupplierDebtAccount(
    supplierId: string,
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PartyDebtAccountFilterCriteria,
  ): Promise<ApiResponse<Page<IPartyDebtAccount>>>;
  getCustomerDebtAccount(
    customerId: string,
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PartyDebtAccountFilterCriteria,
  ): Promise<ApiResponse<Page<IPartyDebtAccount>>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/debt-accounts", {
  auth: true,
});

class DebtAccountService implements IDebtAccountService {
  async getUnpaidSupplierDebtAccount(
    supplierId: string,
  ): Promise<ApiResponse<IPartyDebtAccount[]>> {
    return (await apiClient.get(`/supplier/${supplierId}/unpaid`)).data;
  }

  async getUnpaidCustomerDebtAccount(
    customerId: string,
  ): Promise<ApiResponse<IPartyDebtAccount[]>> {
    return (await apiClient.get(`/customer/${customerId}/unpaid`)).data;
  }

  async getSupplierDebtAccount(
    supplierId: string,
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PartyDebtAccountFilterCriteria,
  ): Promise<ApiResponse<Page<IPartyDebtAccount>>> {
    return (
      await apiClient.get(`/supplier/${supplierId}/page`, {
        params: {
          ...pagination,
          ...filter,
          sortBy: sort?.sortBy !== "" ? sort?.sortBy : undefined,
          direction: sort?.direction !== "" ? sort?.direction : undefined,
        },
      })
    ).data;
  }

  async getCustomerDebtAccount(
    customerId: string,
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PartyDebtAccountFilterCriteria,
  ): Promise<ApiResponse<Page<IPartyDebtAccount>>> {
    return (
      await apiClient.get(`/customer/${customerId}/page`, {
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

export const debtAccountService = new DebtAccountService();
