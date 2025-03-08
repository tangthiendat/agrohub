import { create } from "zustand";
import { createJSONStorage, devtools, persist } from "zustand/middleware";
import { immer } from "zustand/middleware/immer";
import type {} from "@redux-devtools/extension";
import dayjs from "dayjs";
import { DiscountType } from "../common/enums";
import {
  IProduct,
  IProductUnit,
  ISupplier,
  IUserInfo,
  IWarehouse,
} from "../interfaces";
import { getFinalAmount } from "../utils/data";

export interface ImportInvoiceDetailState {
  product: IProduct;
  productUnit: IProductUnit;
  quantity: number;
  unitPrice: number;
}

export interface ImportInvoiceState {
  supplier: ISupplier;
  warehouse: IWarehouse;
  user: IUserInfo;
  createdDate: string;
  totalAmount: number;
  discountValue: number;
  discountType: DiscountType;
  vatRate: number;
  finalAmount: number;
  importInvoiceDetails: ImportInvoiceDetailState[];
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
  importInvoiceDetails: [] as ImportInvoiceDetailState[],
};

export const useImportInvoiceStore = create<ImportInvoiceState>()(
  devtools(
    persist(
      immer((set) => ({
        ...initialState,
        addDetail: (product: IProduct) => {
          set(
            (state) => {
              const existingDetail = state.importInvoiceDetails.find(
                (d) => d.product.productId === product.productId,
              );
              if (existingDetail) {
                existingDetail.quantity += 1;
              } else {
                state.importInvoiceDetails.push({
                  product,
                  productUnit: product.productUnits.find((pu) => pu.isDefault)!,
                  quantity: 1,
                  unitPrice: 0,
                });
              }
            },
            false,
            "addDetail",
          );
        },
        updateProductUnit: (productId: string, productUnitId: string) => {
          set(
            (state) => {
              const detail = state.importInvoiceDetails.find(
                (d) => d.product.productId === productId,
              );
              if (detail) {
                detail.productUnit = detail.product.productUnits.find(
                  (pu) => pu.productUnitId === productUnitId,
                )!;
              }
            },
            false,
            "updateProductUnit",
          );
        },
        updateQuantity: (productId: string, quantity: number) => {
          set(
            (state) => {
              const detail = state.importInvoiceDetails.find(
                (d) => d.product.productId === productId,
              );
              if (detail) {
                detail.quantity = quantity;
                // Update total amount and final amount
                state.totalAmount = state.importInvoiceDetails.reduce(
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
              const detail = state.importInvoiceDetails.find(
                (d) => d.product.productId === productId,
              );
              if (detail) {
                detail.unitPrice = unitPrice;
                // Update total amount and final amount
                state.totalAmount = state.importInvoiceDetails.reduce(
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
        setVatRate: (vatRate: number) => {
          set((state) => {
            state.vatRate = vatRate;
            state.finalAmount = getFinalAmount(
              state.totalAmount,
              state.discountValue,
              state.discountType,
              vatRate,
            );
          });
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

        reset: () => {
          set(
            (state) => {
              state.supplier = {} as ISupplier;
              state.totalAmount = 0;
              state.discountValue = 0;
              state.discountType = DiscountType.AMOUNT;
              state.finalAmount = 0;
              state.vatRate = 0;
              state.importInvoiceDetails = [];
            },
            false,
            "reset",
          );
        },
        deleteDetail(productId) {
          set(
            (state) => {
              state.importInvoiceDetails = state.importInvoiceDetails.filter(
                (d) => d.product.productId !== productId,
              );
            },
            false,
            "deleteDetail",
          );
        },
      })),
      {
        name: "import-invoice-store",
        storage: createJSONStorage(() => sessionStorage),
      },
    ),
    { name: "ImportInvoiceStore" },
  ),
);
