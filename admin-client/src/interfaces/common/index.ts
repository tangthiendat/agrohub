import { GetProp, UploadProps } from "antd";

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
