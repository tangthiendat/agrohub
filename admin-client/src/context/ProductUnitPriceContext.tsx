import { createContext, PropsWithChildren, useContext, useState } from "react";
import { IProductUnitPrice } from "../interfaces";

interface ProductUnitPriceContextValue {
  currentProductUnitPrice?: Partial<IProductUnitPrice>;
  setCurrentProductUnitPrice: React.Dispatch<
    React.SetStateAction<Partial<IProductUnitPrice> | undefined>
  >;
}

const ProductUnitPriceContext =
  createContext<ProductUnitPriceContextValue | null>(null);

const ProductUnitPriceContextProvider: React.FC<PropsWithChildren> = ({
  children,
}) => {
  const [currentProductUnitPrice, setCurrentProductUnitPrice] = useState<
    Partial<IProductUnitPrice> | undefined
  >({});

  return (
    <ProductUnitPriceContext.Provider
      value={{ currentProductUnitPrice, setCurrentProductUnitPrice }}
    >
      {children}
    </ProductUnitPriceContext.Provider>
  );
};

const useProductUnitPrice = () => {
  const context = useContext(ProductUnitPriceContext);
  if (!context) {
    throw new Error("useTraveler must be used within a TravelerProvider");
  }
  return context;
};

export { ProductUnitPriceContextProvider, useProductUnitPrice };
