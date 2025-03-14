import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import {
  ApiResponse,
  IUnit,
  Page,
  PaginationParams,
  SortParams,
} from "../../interfaces";

interface IUnitService {
  create(newUnit: Omit<IUnit, "unitId">): Promise<ApiResponse<void>>;
  getPage(
    pagination: PaginationParams,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<IUnit>>>;
  getAll(): Promise<ApiResponse<IUnit[]>>;
  update(unitId: number, updatedUnit: IUnit): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/units", {
  auth: true,
});

class UnitService implements IUnitService {
  async create(newUnit: Omit<IUnit, "unitId">): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newUnit)).data;
  }

  async getPage(
    pagination: PaginationParams,
    sort?: SortParams,
  ): Promise<ApiResponse<Page<IUnit>>> {
    return (
      await apiClient.get("/page", {
        params: {
          ...pagination,
          sortBy: sort?.sortBy !== "" ? sort?.sortBy : undefined,
          direction: sort?.direction !== "" ? sort?.direction : undefined,
        },
      })
    ).data;
  }

  async getAll(): Promise<ApiResponse<IUnit[]>> {
    return (await apiClient.get("")).data;
  }

  async update(unitId: number, updatedUnit: IUnit): Promise<ApiResponse<void>> {
    return (await apiClient.put(`/${unitId}`, updatedUnit)).data;
  }
}

export const unitService = new UnitService();
