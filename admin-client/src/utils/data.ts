import { snakeCase } from "lodash";
import { DiscountType } from "../common/enums";
import { IProduct, IProductUnitPrice } from "../interfaces";
import dayjs from "dayjs";

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

export function getFinalAmount(
  totalAmount: number,
  discountValue: number,
  discountType: DiscountType,
  vatRate: number,
): number {
  if (discountType === DiscountType.PERCENTAGE) {
    return (
      totalAmount -
      totalAmount * (discountValue / 100) +
      getVATValue(totalAmount, vatRate)
    );
  }
  return totalAmount + getVATValue(totalAmount, vatRate) - discountValue;
}

export function getCurrentProductUnitPrice(
  product: IProduct,
  productUnitId: string,
): IProductUnitPrice {
  const productUnit = product.productUnits.find(
    (pu) => pu.productUnitId === productUnitId,
  );
  const sortedProductUnitPrices = productUnit!.productUnitPrices!.sort((a, b) =>
    dayjs(b.validFrom).diff(dayjs(a.validFrom)),
  );
  return sortedProductUnitPrices!.find((pup) =>
    dayjs().isAfter(pup.validFrom),
  )!;
}
