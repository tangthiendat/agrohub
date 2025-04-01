import { AxiosInstance } from "axios";
import {
  ApiResponse,
  Page,
  PaginationParams,
  SortParams,
} from "../../interfaces";
import { CustomerFilterCriteria, ICustomer } from "../../interfaces/customer";
import { createApiClient } from "../../config/axios/api-client";

interface ICustomerService {
  create(newCustomer: ICustomer): Promise<ApiResponse<void>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: CustomerFilterCriteria,
  ): Promise<ApiResponse<Page<ICustomer>>>;

  update(
    customerId: string,
    updatedCustomer: ICustomer,
  ): Promise<ApiResponse<void>>;
  updateStatus(customerId: string, active: boolean): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/customers", {
  auth: true,
});

class CustomerService implements ICustomerService {
  async create(newCustomer: ICustomer): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newCustomer)).data;
  }

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
    filter?: CustomerFilterCriteria,
  ): Promise<ApiResponse<Page<ICustomer>>> {
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
    customerId: string,
    updatedCustomer: ICustomer,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.put(`/${customerId}`, updatedCustomer)).data;
  }

  async updateStatus(
    customerId: string,
    active: boolean,
  ): Promise<ApiResponse<void>> {
    return (await apiClient.patch(`/${customerId}/status`, { active })).data;
  }
}

export const customerService = new CustomerService();
