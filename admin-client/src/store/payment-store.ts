import type {} from "@redux-devtools/extension";
import dayjs from "dayjs";
import { create } from "zustand";
import { devtools } from "zustand/middleware";
import { immer } from "zustand/middleware/immer";
import { IPartyDebtAccount, IUserInfo, IWarehouse } from "../interfaces";

export interface PaymentDetailState {
  debtAccount: IPartyDebtAccount;
  paymentAmount: number;
}

export interface PaymentState {
  // supplier: ISupplier;
  warehouse: IWarehouse;
  user: IUserInfo;
  createdDate: string;
  totalPaidAmount: number;
  paymentMethodId: string;
  paymentDetails: PaymentDetailState[];
  // setSupplier: (supplier: ISupplier) => void;
  setWarehouse: (warehouse: IWarehouse) => void;
  setUser: (user: IUserInfo) => void;
  setPaymentMethodId: (paymentMethodId: string) => void;
  initPaymentDetails: (debtAccounts: IPartyDebtAccount[]) => void;
  setTotalPaidAmount: (totalPaidAmount: number) => void;
  updatePaymentDetail: (debtAccountId: string, paymentAmount: number) => void;
  reset: () => void;
}

const initialState = {
  // supplier: {} as ISupplier,
  warehouse: {} as IWarehouse,
  user: {} as IUserInfo,
  createdDate: dayjs().format("YYYY-MM-DD"),
  totalPaidAmount: 0,
  paymentMethodId: "",
  paymentDetails: [] as PaymentDetailState[],
};

export const usePaymentStore = create<PaymentState>()(
  devtools(
    immer((set) => ({
      ...initialState,
      // setSupplier: (supplier) =>
      //   set(
      //     (state) => {
      //       state.supplier = supplier;
      //     },
      //     false,
      //     "setSupplier",
      //   ),
      setWarehouse: (warehouse) =>
        set(
          (state) => {
            state.warehouse = warehouse;
          },
          false,
          "setWarehouse",
        ),
      setUser: (user) =>
        set(
          (state) => {
            state.user = user;
          },
          false,
          "setUser",
        ),
      setPaymentMethodId: (paymentMethodId) =>
        set(
          (state) => {
            state.paymentMethodId = paymentMethodId;
          },
          false,
          "setPaymentMethodId",
        ),
      initPaymentDetails: (debtAccounts) =>
        set(
          (state) => {
            state.paymentDetails = debtAccounts.map((debtAccount) => ({
              debtAccount,
              paymentAmount: 0,
            }));
          },
          false,
          "initPaymentDetails",
        ),
      setTotalPaidAmount: (totalPaidAmount) =>
        set(
          (state) => {
            state.totalPaidAmount = totalPaidAmount;

            let remainingAmount = totalPaidAmount;

            // Allocate sequentially to each payment detail
            state.paymentDetails = state.paymentDetails.map((detail) => {
              if (remainingAmount <= 0) {
                return { ...detail, paymentAmount: 0 };
              }

              const debtAmount = detail.debtAccount.totalAmount;
              const allocation = Math.min(debtAmount, remainingAmount);
              remainingAmount -= allocation;

              return {
                ...detail,
                paymentAmount: Math.round(allocation * 100) / 100,
              };
            });
          },
          false,
          "setTotalPaidAmount",
        ),
      updatePaymentDetail: (debtAccountId, paymentAmount) =>
        set(
          (state) => {
            const detail = state.paymentDetails.find(
              (detail) => detail.debtAccount.sourceId === debtAccountId,
            );

            if (detail) {
              detail.paymentAmount = paymentAmount;
            }
          },
          false,
          "updatePaymentDetail",
        ),
      reset: () =>
        set(() => ({
          ...initialState,
        })),
    })),
    { name: "PaymentStore" },
  ),
);
