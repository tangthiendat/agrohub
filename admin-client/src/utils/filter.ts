import { SortOrder } from "antd/es/table/interface";

export function getDefaultSortOrder(
  searchParams: URLSearchParams,
  columnKey: string,
): SortOrder | undefined {
  const sortBy = searchParams.get("sortBy");
  const direction = searchParams.get("direction");

  if (sortBy === columnKey) {
    return direction === "asc"
      ? "ascend"
      : direction === "desc"
        ? "descend"
        : undefined;
  }
  return undefined;
}

export function getSortDirection(sortOrder: string): string | undefined {
  return sortOrder === "ascend"
    ? "asc"
    : sortOrder === "descend"
      ? "desc"
      : undefined;
}

export function getDefaultFilterValue(
  searchParams: URLSearchParams,
  key: string,
): string[] | undefined {
  const value = searchParams.get(key);
  return value ? value.split(",") : undefined;
}
