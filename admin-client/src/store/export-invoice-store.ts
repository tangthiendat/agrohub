import type {} from "@redux-devtools/extension";
import dayjs from "dayjs";
import { create } from "zustand";
import Decimal from "decimal.js";
import { createJSONStorage, devtools, persist } from "zustand/middleware";
import { immer } from "zustand/middleware/immer";
import { DiscountType } from "../common/enums";
import {
  IProduct,
  IProductBatch,
  IProductBatchLocation,
  IProductUnit,
  ISupplier,
  IUserInfo,
  IWarehouse,
} from "../interfaces";
import {
  getCurrentProductUnitPrice,
  getFinalAmount,
  getProductStockQuantity,
} from "../utils/data";

export interface SelectedLocationState {
  location: IProductBatchLocation;
  quantity: number;
}

export interface SelectedBatchState {
  productBatch: IProductBatch;
  quantity: number;
  selectedLocations: SelectedLocationState[];
}

export interface ExportInvoiceDetailState {
  product: IProduct;
  productUnit: IProductUnit;
  quantity: number;
  unitPrice: number;
  selectedBatches: SelectedBatchState[];
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
  initDetailBatch: (productId: string, productBatch: IProductBatch[]) => void;
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
  vatRate: 10,
  exportInvoiceDetails: [] as ExportInvoiceDetailState[],
};

const allocateQuantities = (
  detail: ExportInvoiceDetailState,
  difference: number,
) => {
  if (difference > 0) {
    // Increasing quantity - add sequentially from start to end
    let remainingToAdd = difference;
    for (
      let i = 0;
      i < detail.selectedBatches.length && remainingToAdd > 0;
      i++
    ) {
      const selectedBatch = detail.selectedBatches[i];
      for (
        let j = 0;
        j < selectedBatch.selectedLocations.length && remainingToAdd > 0;
        j++
      ) {
        const selectedLocation = selectedBatch.selectedLocations[j];
        const availableSpace =
          selectedLocation.location.quantity - selectedLocation.quantity;

        const toAdd = Math.min(availableSpace, remainingToAdd);
        selectedLocation.quantity += toAdd;
        remainingToAdd -= toAdd;
      }
      // Recalculate batch total quantity
      let batchTotal = 0;
      for (let j = 0; j < selectedBatch.selectedLocations.length; j++) {
        batchTotal += selectedBatch.selectedLocations[j].quantity;
      }
      selectedBatch.quantity = batchTotal;
    }
  } else if (difference < 0) {
    // Decreasing quantity - remove sequentially from end to start
    let remainingToRemove = -difference;
    for (
      let i = detail.selectedBatches.length - 1;
      i >= 0 && remainingToRemove > 0;
      i--
    ) {
      const selectedBatch = detail.selectedBatches[i];
      for (
        let j = selectedBatch.selectedLocations.length - 1;
        j >= 0 && remainingToRemove > 0;
        j--
      ) {
        const selectedLocation = selectedBatch.selectedLocations[j];
        const toRemove = Math.min(selectedLocation.quantity, remainingToRemove);
        selectedLocation.quantity -= toRemove;
        remainingToRemove -= toRemove;
      }
      // Recalculate batch total quantity
      let batchTotal = 0;
      for (let j = 0; j < selectedBatch.selectedLocations.length; j++) {
        batchTotal += selectedBatch.selectedLocations[j].quantity;
      }
      selectedBatch.quantity = batchTotal;
    }
  }
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
                  selectedBatches: [],
                });
              }

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
              if (state.exportInvoiceDetails.length === 0) {
                state.totalAmount = 0;
                state.discountValue = 0;
                state.discountType = DiscountType.AMOUNT;
                state.vatRate = 10;
                state.finalAmount = 0;
                state.exportInvoiceDetails = [];
              }
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
                  // Get the last product unit
                  const lastProductUnit = detail.productUnit;

                  const newConversionFactor = new Decimal(
                    newProductUnit.conversionFactor,
                  );
                  const lastConversionFactor = new Decimal(
                    lastProductUnit.conversionFactor,
                  );

                  const conversionRatio =
                    newConversionFactor.div(lastConversionFactor);

                  // Update product unit and price
                  detail.productUnit = newProductUnit;
                  detail.unitPrice = getCurrentProductUnitPrice(
                    detail.product,
                    newProductUnit.productUnitId,
                  );

                  const previousQuantity = detail.quantity;
                  detail.quantity = 1;

                  // Convert quantities in selectedBatches and selectedLocations
                  detail.selectedBatches.forEach((batch) => {
                    // Convert batch quantity
                    const batchQuantity = new Decimal(batch.quantity);
                    const convertedBatchQuantity = batchQuantity
                      .mul(conversionRatio)
                      .toDecimalPlaces(2, Decimal.ROUND_HALF_UP)
                      .toNumber();
                    batch.quantity = convertedBatchQuantity;

                    // Convert each location's quantity
                    batch.selectedLocations.forEach((location) => {
                      const locationQuantity = new Decimal(location.quantity);
                      const convertedLocationQuantity = locationQuantity
                        .mul(conversionRatio)
                        .toDecimalPlaces(2, Decimal.ROUND_HALF_UP)
                        .toNumber();
                      location.quantity = convertedLocationQuantity;
                    });
                  });

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

                  // Allocate quantities to batches and locations
                  const convertedQuantity = getProductStockQuantity(
                    detail.product,
                    detail.productUnit.productUnitId,
                    detail.quantity,
                  );

                  const difference =
                    convertedQuantity -
                    getProductStockQuantity(
                      detail.product,
                      detail.productUnit.productUnitId,
                      previousQuantity,
                    );

                  allocateQuantities(detail, difference);
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
                const previousQuantity = detail.quantity;

                // Convert the quantity to the stock unit using getProductStockQuantity
                const convertedQuantity = getProductStockQuantity(
                  detail.product,
                  detail.productUnit.productUnitId,
                  quantity,
                );

                detail.quantity = quantity; // Update the detail's quantity

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

                const difference =
                  convertedQuantity -
                  getProductStockQuantity(
                    detail.product,
                    detail.productUnit.productUnitId,
                    previousQuantity,
                  );

                allocateQuantities(detail, difference);
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
        initDetailBatch: (productId: string, productBatch: IProductBatch[]) => {
          set(
            (state) => {
              const detail = state.exportInvoiceDetails.find(
                (d) => d.product.productId === productId,
              );
              if (detail) {
                detail.selectedBatches = productBatch.map(
                  (productBatch, index) => {
                    if (index === 0) {
                      return {
                        productBatch,
                        quantity: 1,
                        selectedLocations: productBatch.batchLocations.map(
                          (location, index) => {
                            if (index === 0) {
                              return {
                                location,
                                quantity: 1,
                              };
                            }
                            return {
                              location,
                              quantity: 0,
                            };
                          },
                        ),
                      };
                    }

                    return {
                      productBatch,
                      quantity: 0,
                      selectedLocations: productBatch.batchLocations.map(
                        (location) => ({
                          location,
                          quantity: 0,
                        }),
                      ),
                    };
                  },
                );
              }
            },
            false,
            "initDetailBatch",
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
        reset: () =>
          set(
            (state) => {
              state.totalAmount = 0;
              state.discountValue = 0;
              state.discountType = DiscountType.AMOUNT;
              state.vatRate = 10;
              state.finalAmount = 0;
              state.exportInvoiceDetails = [];
            },
            false,
            "reset",
          ),
      })),
      {
        name: "export-invoice-store",
        storage: createJSONStorage(() => sessionStorage),
      },
    ),
  ),
);
