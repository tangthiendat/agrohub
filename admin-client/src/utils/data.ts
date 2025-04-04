import dayjs from "dayjs";
import { snakeCase } from "lodash";
import { DiscountType } from "../common/enums";
import { IProduct, IProductLocation } from "../interfaces";
import { parseCurrency } from "./number";

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

export function getVATValue(totalAmount: number, vatRate: number): number {
  return totalAmount * (vatRate / 100);
}

export function getDiscountValue(
  totalAmount: number,
  discountValue: number,
  discountType: DiscountType,
): number {
  if (discountType === DiscountType.PERCENTAGE) {
    return totalAmount * (discountValue / 100);
  }
  return discountValue;
}

export function getFinalAmount(
  totalAmount: number,
  discountValue: number,
  discountType: DiscountType,
  vatRate: number,
): number {
  const discount = getDiscountValue(totalAmount, discountValue, discountType);
  const vat = getVATValue(totalAmount, vatRate);
  return totalAmount - discount + vat;
}

export function getCurrentProductUnitPrice(
  product: IProduct,
  productUnitId: string,
): number {
  const productUnit = product.productUnits.find(
    (pu) => pu.productUnitId === productUnitId,
  );
  const sortedProductUnitPrices = productUnit!.productUnitPrices!.sort((a, b) =>
    dayjs(b.validFrom).diff(dayjs(a.validFrom)),
  );
  return sortedProductUnitPrices!.find((pup) => dayjs().isAfter(pup.validFrom))!
    .price;
}

export function getLocationName(location: IProductLocation) {
  return `${location.rackName}${location.rowNumber}.${location.columnNumber}`;
}

export function getTransactionAmount(amount: string): string {
  if (parseCurrency(amount) > 0) {
    return `+${amount}`;
  }
  return `${amount}`;
}
