import { SortOrder } from "antd/es/table/interface";
import { HttpMethod } from "../common/enums";

export function getHttpMethodColor(httpMethod: HttpMethod): string {
  switch (httpMethod) {
    case HttpMethod.GET:
      return "#60AFFE";
    case HttpMethod.POST:
      return "#4ACB90";
    case HttpMethod.PUT:
      return "#FCA12F";
    case HttpMethod.PATCH:
      return "#4FE2C2";
    case HttpMethod.DELETE:
      return "#F93F3E";
    default:
      return "#9E9E9E";
  }
}

export function getFilterIconColor(filtered: boolean): string {
  return filtered ? "#FFC107" : "#fff";
}

export function getSortUpIconColor(sortOrder: SortOrder | undefined): string {
  return sortOrder === "ascend" ? "#FFC107" : "#fff";
}

export function getSortDownIconColor(sortOrder: SortOrder | undefined): string {
  return sortOrder === "descend" ? "#FFC107" : "#fff";
}
