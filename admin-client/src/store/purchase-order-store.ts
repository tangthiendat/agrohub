import { create } from "zustand";
import { createJSONStorage, devtools, persist } from "zustand/middleware";
import { immer } from "zustand/middleware/immer";
import type {} from "@redux-devtools/extension";
import { DiscountType } from "../common/enums";
import {
  IProduct,
  IProductUnit,
  ISupplier,
  IUserInfo,
  IWarehouse,
} from "../interfaces";
import dayjs from "dayjs";

export interface PurchaseOrderDetailState {
  product: IProduct;
  productUnit: IProductUnit;
  quantity: number;
}

export interface PurchaseOrderState {
  supplier: ISupplier;
  warehouse: IWarehouse;
  user: IUserInfo;
  orderDate: string;
  totalAmount: number;
  discountValue: number;
  discountType: DiscountType;
  vatRate: number;
  finalAmount: number;
  purchaseOrderDetails: PurchaseOrderDetailState[];
  addDetail: (product: IProduct) => void;
  deleteDetail: (productId: string) => void;
  updateProductUnit: (productId: string, productUnitId: string) => void;
  updateQuantity: (productId: string, quantity: number) => void;
  setSupplier: (supplier: ISupplier) => void;
  setWarehouse: (warehouse: IWarehouse) => void;
  setUser: (user: IUserInfo) => void;
  setDiscountType: (discountType: DiscountType) => void;
  reset: () => void;
}

const initialState = {
  supplier: {} as ISupplier,
  warehouse: {} as IWarehouse,
  user: {} as IUserInfo,
  orderDate: dayjs().format("YYYY-MM-DD"),
  totalAmount: 0,
  discountValue: 0,
  discountType: DiscountType.AMOUNT,
  finalAmount: 0,
  vatRate: 0,
  purchaseOrderDetails: [] as PurchaseOrderDetailState[],
};

export const usePurchaseOrderStore = create<PurchaseOrderState>()(
  devtools(
    persist(
      immer((set) => ({
        ...initialState,
        addDetail: (product: IProduct) => {
          set(
            (state) => {
              const existingDetail = state.purchaseOrderDetails.find(
                (d) => d.product.productId === product.productId,
              );
              if (existingDetail) {
                existingDetail.quantity += 1;
              } else {
                state.purchaseOrderDetails.push({
                  product,
                  productUnit: product.productUnits.find((pu) => pu.isDefault)!,
                  quantity: 1,
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
              const detail = state.purchaseOrderDetails.find(
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
              const detail = state.purchaseOrderDetails.find(
                (d) => d.product.productId === productId,
              );
              if (detail) {
                detail.quantity = quantity;
              }
            },
            false,
            "updateQuantity",
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
        setDiscountType: (discountType: DiscountType) => {
          set(
            (state) => {
              state.discountType = discountType;
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
              state.purchaseOrderDetails = [];
            },
            false,
            "reset",
          );
        },
        deleteDetail(productId) {
          set(
            (state) => {
              state.purchaseOrderDetails = state.purchaseOrderDetails.filter(
                (d) => d.product.productId !== productId,
              );
            },
            false,
            "deleteDetail",
          );
        },
      })),
      {
        name: "purchase-order-storage",
        storage: createJSONStorage(() => sessionStorage),
      },
    ),
    { name: "PurchaseOrderStore" },
  ),
);
