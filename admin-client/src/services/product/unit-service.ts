import { AxiosInstance } from "axios";
import { createApiClient } from "../../config/axios/api-client";
import { ApiResponse, IUnit } from "../../interfaces";

interface IUnitService {
  create(newUnit: Omit<IUnit, "unitId">): Promise<ApiResponse<void>>;
}

const apiClient: AxiosInstance = createApiClient("api/v1/units", {
  auth: true,
});

class UnitService implements IUnitService {
  async create(newUnit: Omit<IUnit, "unitId">): Promise<ApiResponse<void>> {
    return (await apiClient.post("", newUnit)).data;
  }
}

export const unitService = new UnitService();
