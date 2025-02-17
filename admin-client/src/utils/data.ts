import { snakeCase } from "lodash";

export function convertKeysToSnakeCase<T>(obj: T): T {
  if (Array.isArray(obj)) {
    return obj.map((item) => convertKeysToSnakeCase(item)) as T;
  } else if (obj && typeof obj === "object") {
    return Object.keys(obj).reduce<Record<string, unknown>>((acc, key) => {
      const snakeCaseKey = snakeCase(key);
      acc[snakeCaseKey] = convertKeysToSnakeCase(
        (obj as Record<string, unknown>)[key],
      );
      return acc;
    }, {}) as T;
  }
  return obj;
}
