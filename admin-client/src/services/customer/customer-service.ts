import { AxiosInstance } from "axios";
import { ApiResponse } from "../../interfaces";
import { ICustomer } from "../../interfaces/customer";
import { createApiClient } from "../../config/axios/api-client";

interface ICustomerService {
  create(newCustomer: ICustomer): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/customers", {
  auth: true,
});

class CustomerService implements ICustomerService {
  async create(newCustomer: ICustomer): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newCustomer)).data;
  }
}

export const customerService = new CustomerService();
