import { Gender, HttpMethod } from "../../common/enums";

export interface IAuthRequest {
  email: string;
  password: string;
}
export interface IAuthResponse {
  accessToken: string;
}

export interface IPermission {
  permissionId: number;
  permissionName: string;
  apiPath: string;
  httpMethod: HttpMethod;
  module: string;
  description?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface IRole {
  roleId: number;
  roleName: string;
  active: boolean;
  description?: string;
  permissions: IPermission[];
  createdAt: string;
  updatedAt?: string;
}

export interface IUser {
  userId: string;
  fullName: string;
  email: string;
  password?: string;
  dob: string;
  gender: Gender;
  active: boolean;
  phoneNumber: string;
  role: IRole;
  createdAt: string;
  updatedAt?: string;
}

export interface PermissionFilterCriteria {
  httpMethod?: string;
  module?: string;
}

export interface UserFilterCriteria {
  gender?: string;
  query?: string;
  active?: boolean;
  role?: string;
}
