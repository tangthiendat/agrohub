import { useSearchParams } from "react-router";
import { useQuery } from "@tanstack/react-query";
import Access from "../features/auth/Access";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import { PERMISSIONS } from "../common/constants";
import { PaginationParams, SortParams } from "../interfaces";
import { productBatchService } from "../services";
import ProductBatchTable from "../features/inventory/batch/ProductBatchTable";

const ProductBatches: React.FC = () => {
  useTitle("Lô hàng");
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
    queryKey: ["product-locations", pagination, sort].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => productBatchService.getPage(pagination, sort),
  });
  return (
    <Access permission={PERMISSIONS[Module.PRODUCT_BATCH].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Lô hàng</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex gap-3">
              {/* <Search
                placeholder="Nhập tên vị trí (ví dụ A1.2)"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              /> */}
            </div>
          </div>
        </div>
        <ProductBatchTable
          productBatchPage={data?.payload}
          isLoading={isLoading}
        />
      </div>
    </Access>
  );
};

export default ProductBatches;
