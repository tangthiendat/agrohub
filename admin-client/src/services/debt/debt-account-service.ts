import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, IPartyDebtAccount } from "../../interfaces";

export interface IDebtAccountService {
  getUnpaidPartyDebtAccount(
    partyId: string,
  ): Promise<ApiResponse<IPartyDebtAccount[]>>;
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
}

export const debtAccountService = new DebtAccountService();
