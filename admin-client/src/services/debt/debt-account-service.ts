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
  getUnpaidPartyDebtAccount(
    partyId: string,
  ): Promise<ApiResponse<IPartyDebtAccount[]>>;
  getPartyDebtAccount(
    partyId: string,
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PartyDebtAccountFilterCriteria,
  ): Promise<ApiResponse<Page<IPartyDebtAccount>>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/debt-accounts", {
  auth: true,
});

class DebtAccountService implements IDebtAccountService {
  async getUnpaidPartyDebtAccount(
    partyId: string,
  ): Promise<ApiResponse<IPartyDebtAccount[]>> {
    return (await apiClient.get(`/party/${partyId}/unpaid`)).data;
  }

  async getPartyDebtAccount(
    partyId: string,
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: PartyDebtAccountFilterCriteria,
  ): Promise<ApiResponse<Page<IPartyDebtAccount>>> {
    return (
      await apiClient.get(`/party/${partyId}/page`, {
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
