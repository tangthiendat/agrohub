import { GetProp, UploadProps } from "antd";
import { TrendType } from "../../common/enums";

export interface ErrorDetail {
  field: string;
  message: string;
}

export interface ApiError {
  errorCode: string;
  errorType: string;
  message: string;
  details: ErrorDetail[];
}

export interface ApiResponse<T> {
  success: boolean;
  status: number;
  message?: string;
  payload: T;
  error?: ApiError;
}

export interface PaginationParams {
  page: number;
  pageSize: number;
}

export interface PaginationMeta {
  page: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

export interface Page<T> {
  content: T[];
  meta: PaginationMeta;
}

export interface SortParams {
  sortBy: string;
  direction: string;
}

export interface Auditable {
  createdAt?: string;
  updatedAt?: string;
}

export type FileType = Parameters<GetProp<UploadProps, "beforeUpload">>[0];

export interface IStatsCardValue {
  value: number;
  changePercentage: number;
  trend: TrendType;
}

export interface ActivityChartData {
  label: string;
  imports: number;
  exports: number;
}

export interface StatisticFilterCriteria {
  startDate?: string;
  endDate?: string;
  type?: string;
}

export interface CategoryInventoryData {
  label: string;
  value: number;
}
