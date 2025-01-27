import { HttpMethod, Module } from "../enums";

export const PERMISSIONS = {
  [Module.PERMISSION]: {
    GET_PAGE: {
      apiPath: "/user-service/api/v1/permissions/page",
      httpMethod: HttpMethod.GET,
    },
    GET_ALL: {
      apiPath: "/user-service/api/v1/permissions",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/user-service/api/v1/permissions",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/user-service/api/v1/permissions/{id}",
      httpMethod: HttpMethod.PUT,
    },
    UPDATE_STATUS: {
      apiPath: "/user-service/api/v1/permissions/{id}/status",
      httpMethod: HttpMethod.PATCH,
    },
    DELETE: {
      apiPath: "/user-service/api/v1/permissions/{id}",
      httpMethod: HttpMethod.DELETE,
    },
  },
  [Module.ROLE]: {
    GET_PAGE: {
      apiPath: "/user-service/api/v1/roles/page",
      httpMethod: HttpMethod.GET,
    },
    GET_ALL: {
      apiPath: "/user-service/api/v1/roles",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/user-service/api/v1/roles",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/user-service/api/v1/roles/{id}",
      httpMethod: HttpMethod.PUT,
    },
    UPDATE_STATUS: {
      apiPath: "/user-service/api/v1/roles/{id}/status",
      httpMethod: HttpMethod.PATCH,
    },
  },
  [Module.USER]: {
    GET_PAGE: {
      apiPath: "/user-service/api/v1/users/page",
      httpMethod: HttpMethod.GET,
    },
    CREATE: {
      apiPath: "/user-service/api/v1/users",
      httpMethod: HttpMethod.POST,
    },
    UPDATE: {
      apiPath: "/user-service/api/v1/users/{id}",
      httpMethod: HttpMethod.PUT,
    },
    UPDATE_STATUS: {
      apiPath: "/user-service/api/v1/users/{id}/status",
      httpMethod: HttpMethod.PATCH,
    },
  },
};
