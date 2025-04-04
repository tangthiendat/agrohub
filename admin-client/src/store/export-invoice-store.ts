import dayjs from "dayjs";
import { DiscountType } from "../common/enums";
import { createJSONStorage, devtools, persist } from "zustand/middleware";
import { immer } from "zustand/middleware/immer";
import type {} from "@redux-devtools/extension";
import {
  IProduct,
  IProductUnit,
  ISupplier,
  IUserInfo,
  IWarehouse,
} from "../interfaces";
import { create } from "zustand";
import { getCurrentProductUnitPrice, getFinalAmount } from "../utils/data";

export interface ExportInvoiceDetailState {
  product: IProduct;
  productUnit: IProductUnit;
  quantity: number;
  unitPrice: number;
}

export interface ExportInvoiceState {
  supplier: ISupplier;
  warehouse: IWarehouse;
  user: IUserInfo;
  createdDate: string;
  totalAmount: number;
  discountValue: number;
  discountType: DiscountType;
  vatRate: number;
  finalAmount: number;
  exportInvoiceDetails: ExportInvoiceDetailState[];
  addDetail: (product: IProduct) => void;
  deleteDetail: (productId: string) => void;
  updateProductUnit: (productId: string, productUnitId: string) => void;
  updateQuantity: (productId: string, quantity: number) => void;
  updateUnitPrice: (productId: string, unitPrice: number) => void;
  setSupplier: (supplier: ISupplier) => void;
  setWarehouse: (warehouse: IWarehouse) => void;
  setUser: (user: IUserInfo) => void;
  setDiscountValue: (discountValue: number) => void;
  setDiscountType: (discountType: DiscountType) => void;
  setVatRate: (vatRate: number) => void;
  reset: () => void;
}

const initialState = {
  supplier: {} as ISupplier,
  warehouse: {} as IWarehouse,
  user: {} as IUserInfo,
  createdDate: dayjs().format("YYYY-MM-DD"),
  totalAmount: 0,
  discountValue: 0,
  discountType: DiscountType.AMOUNT,
  finalAmount: 0,
  vatRate: 0,
  exportInvoiceDetails: [] as ExportInvoiceDetailState[],
};

export const useExportInvoiceStore = create<ExportInvoiceState>()(
  devtools(
    persist(
      immer((set) => ({
        ...initialState,
        addDetail: (product: IProduct) => {
          set(
            (state) => {
              const defaultProductUnit = product.productUnits.find(
                (pu) => pu.isDefault,
              );
              if (defaultProductUnit) {
                state.exportInvoiceDetails.push({
                  product,
                  productUnit: defaultProductUnit,
                  quantity: 1,
                  unitPrice: getCurrentProductUnitPrice(
                    product,
                    defaultProductUnit.productUnitId,
                  ),
                });
              }
            },
            false,
            "addDetail",
          );
        },
        deleteDetail: (productId: string) => {
          set(
            (state) => {
              state.exportInvoiceDetails = state.exportInvoiceDetails.filter(
                (d) => d.product.productId !== productId,
              );
            },
            false,
            "deleteDetail",
          );
        },
        updateProductUnit: (productId: string, productUnitId: string) => {
          set(
            (state) => {
              const detail = state.exportInvoiceDetails.find(
                (d) => d.product.productId === productId,
              );
              if (detail) {
                const newProductUnit = detail.product.productUnits.find(
                  (pu) => pu.productUnitId === productUnitId,
                );
                if (newProductUnit) {
                  detail.productUnit = newProductUnit;
                  detail.unitPrice = getCurrentProductUnitPrice(
                    detail.product,
                    newProductUnit.productUnitId,
                  );
                }
              }
            },
            false,
            "updateProductUnit",
          );
        },
        updateQuantity: (productId: string, quantity: number) => {
          set(
            (state) => {
              const detail = state.exportInvoiceDetails.find(
                (d) => d.product.productId === productId,
              );
              if (detail) {
                detail.quantity = quantity;
                // Update total amount and final amount
                state.totalAmount = state.exportInvoiceDetails.reduce(
                  (acc, cur) => acc + cur.quantity * cur.unitPrice,
                  0,
                );
                state.finalAmount = getFinalAmount(
                  state.totalAmount,
                  state.discountValue,
                  state.discountType,
                  state.vatRate,
                );
              }
            },
            false,
            "updateQuantity",
          );
        },
        updateUnitPrice: (productId: string, unitPrice: number) => {
          set(
            (state) => {
              const detail = state.exportInvoiceDetails.find(
                (d) => d.product.productId === productId,
              );
              if (detail) {
                detail.unitPrice = unitPrice;
                // Update total amount and final amount
                state.totalAmount = state.exportInvoiceDetails.reduce(
                  (acc, cur) => acc + cur.quantity * cur.unitPrice,
                  0,
                );
                state.finalAmount = getFinalAmount(
                  state.totalAmount,
                  state.discountValue,
                  state.discountType,
                  state.vatRate,
                );
              }
            },
            false,
            "updateUnitPrice",
          );
        },
        setSupplier: (supplier: ISupplier) => {
          set(
            (state) => {
              state.supplier = supplier;
            },
            false,
            "setSupplier",
          );
        },
        setWarehouse: (warehouse: IWarehouse) => {
          set(
            (state) => {
              state.warehouse = warehouse;
            },
            false,
            "setWarehouse",
          );
        },
        setUser: (user: IUserInfo) => {
          set(
            (state) => {
              state.user = user;
            },
            false,
            "setUser",
          );
        },
        setDiscountValue: (discountValue: number) => {
          set(
            (state) => {
              state.discountValue = discountValue;
              // Update final amount
              state.finalAmount = getFinalAmount(
                state.totalAmount,
                discountValue,
                state.discountType,
                state.vatRate,
              );
            },
            false,
            "setDiscountValue",
          );
        },
        setDiscountType: (discountType: DiscountType) => {
          set(
            (state) => {
              state.discountType = discountType;
              state.discountValue = 0;
              state.finalAmount = getFinalAmount(
                state.totalAmount,
                0,
                discountType,
                state.vatRate,
              );
            },
            false,
            "setDiscountType",
          );
        },
        setVatRate: (vatRate: number) => {
          set(
            (state) => {
              state.vatRate = vatRate;
              // Update final amount
              state.finalAmount = getFinalAmount(
                state.totalAmount,
                state.discountValue,
                state.discountType,
                state.vatRate,
              );
            },
            false,
            "setVatRate",
          );
        },
        reset: () => set(initialState),
      })),
      {
        name: "export-invoice-store",
        storage: createJSONStorage(() => sessionStorage),
      },
    ),
  ),
);
