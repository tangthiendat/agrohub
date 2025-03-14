import dayjs from "dayjs";

export function isInDateRange(
  date: string,
  startDate: string,
  endDate: string,
): boolean {
  return dayjs(date).tz().isBetween(startDate, endDate, null, "[]");
}

export function formatTimestamp(timestamp: string): string {
  return dayjs(timestamp).tz().format("DD/MM/YYYY HH:mm:ss");
}

export function formatDate(date: string): string {
  return dayjs(date).tz().format("DD/MM/YYYY");
}
