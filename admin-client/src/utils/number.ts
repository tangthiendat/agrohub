export function formatCurrency(value: number | undefined): string {
  return `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

export function parseCurrency(value: string | undefined): number {
  return (value?.replace(/\$\s?|(,*)/g, "") as unknown as number) || 0;
}

export function roundToTwoDecimalPlaces(value: number): number {
  // return Math.round(value * 100) / 100;
  return Number(value.toFixed(2));
}
