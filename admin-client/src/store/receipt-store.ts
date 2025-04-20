import type {} from "@redux-devtools/extension";
import dayjs from "dayjs";
import { create } from "zustand";
import { devtools } from "zustand/middleware";
import { immer } from "zustand/middleware/immer";
import { IPartyDebtAccount, IUserInfo, IWarehouse } from "../interfaces";

export interface ReceiptDetailState {
  debtAccount: IPartyDebtAccount;
  receiptAmount: number;
}

export interface ReceiptState {
  warehouse: IWarehouse;
  user: IUserInfo;
  createdDate: string;
  totalReceivedAmount: number;
  paymentMethodId: string;
  receiptDetails: ReceiptDetailState[];
  setWarehouse: (warehouse: IWarehouse) => void;
  setUser: (user: IUserInfo) => void;
  setTotalReceivedAmount: (amount: number) => void;
  setPaymentMethodId: (id: string) => void;
  initReceiptDetails: (debtAccounts: IPartyDebtAccount[]) => void;
  reset: () => void;
}

const initialState = {
  warehouse: {} as IWarehouse,
  user: {} as IUserInfo,
  createdDate: dayjs().format("YYYY-MM-DD"),
  totalReceivedAmount: 0,
  paymentMethodId: "",
  receiptDetails: [] as ReceiptDetailState[],
};

export const useReceiptStore = create<ReceiptState>()(
  devtools(
    immer((set) => ({
      ...initialState,
      setWarehouse: (warehouse: IWarehouse) =>
        set(
          (state) => {
            state.warehouse = warehouse;
          },
          false,
          "setWarehouse",
        ),
      setUser: (user: IUserInfo) =>
        set(
          (state) => {
            state.user = user;
          },
          false,
          "setUser",
        ),
      setTotalReceivedAmount: (amount: number) =>
        set(
          (state) => {
            state.totalReceivedAmount = amount;
            if (amount === 0) {
              state.receiptDetails.forEach((detail) => {
                detail.receiptAmount = 0;
              });
              return;
            }

            let remaining = amount;
            for (
              let i = 0;
              i < state.receiptDetails.length && remaining > 0;
              i++
            ) {
              const detail = state.receiptDetails[i];
              const toReceive = Math.min(
                detail.debtAccount.remainingAmount,
                remaining,
              );
              detail.receiptAmount = toReceive;
              remaining -= toReceive;
            }
          },
          false,
          "setTotalReceivedAmount",
        ),
      setPaymentMethodId: (id: string) =>
        set(
          (state) => {
            state.paymentMethodId = id;
          },
          false,
          "setPaymentMethodId",
        ),
      initReceiptDetails: (debtAccounts: IPartyDebtAccount[]) =>
        set(
          (state) => {
            state.receiptDetails = debtAccounts.map((debtAccount) => ({
              debtAccount,
              receiptAmount: 0,
            }));
          },
          false,
          "initReceiptDetails",
        ),
      reset: () =>
        set(
          (state) => {
            state.totalReceivedAmount = 0;
            state.paymentMethodId = "";
            state.receiptDetails = [];
          },
          false,
          "reset",
        ),
    })),
  ),
);
