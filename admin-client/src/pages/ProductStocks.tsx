import { useSearchParams } from "react-router";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import Access from "../features/auth/Access";
import { PaginationParams, SortParams } from "../interfaces";
import { useQuery } from "@tanstack/react-query";
import { productStockService } from "../services";
import ProductStockTable from "../features/inventory/stock/ProductStockTable";

const ProductStocks: React.FC = () => {
  useTitle("Tồn kho");

  const [searchParams, setSearchParams] = useSearchParams();

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const { data, isLoading } = useQuery({
    queryKey: ["product-stocks", pagination, sort].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => productStockService.getPage(pagination, sort),
  });

  return (
    <Access permission={PERMISSIONS[Module.PRODUCT_STOCK].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Tồn kho</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            {/* <div className="flex gap-3">
            <Search
              placeholder="Tìm kiếm theo mã lô hàng"
              defaultValue={searchParams.get("query") || ""}
              enterButton
              allowClear
              onSearch={handleSearch}
            />
          </div> */}
          </div>
        </div>
        <ProductStockTable
          productStockPage={data?.payload}
          isLoading={isLoading}
        />
      </div>
    </Access>
  );
};

export default ProductStocks;
