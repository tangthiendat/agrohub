import { Gender, HttpMethod } from "../../common/enums";
import { Auditable } from "../common";

export interface IAuthRequest {
  email: string;
  password: string;
}
export interface IAuthResponse {
  accessToken: string;
}

export interface IPermission extends Auditable {
  permissionId: number;
  permissionName: string;
  apiPath: string;
  httpMethod: HttpMethod;
  module: string;
  description?: string;
}

export interface IRole extends Auditable {
  roleId: number;
  roleName: string;
  active: boolean;
  description?: string;
  permissions: IPermission[];
}

export interface IUser extends Auditable {
  userId: string;
  fullName: string;
  email: string;
  password?: string;
  dob: string;
  gender: Gender;
  active: boolean;
  warehouseId: number;
  phoneNumber: string;
  role: IRole;
}

export interface PermissionFilterCriteria {
  httpMethod?: string;
  module?: string;
}

export interface UserFilterCriteria {
  gender?: string;
  query?: string;
  active?: boolean;
  roleId?: string;
}
