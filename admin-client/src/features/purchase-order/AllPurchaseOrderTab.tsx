import { useSearchParams } from "react-router";
import { useQuery } from "@tanstack/react-query";
import { SearchProps } from "antd/es/input";
import Search from "antd/es/input/Search";
import PurchaseOrderTable from "./PurchaseOrderTable";
import {
  PaginationParams,
  PurchaseOrderFilterCriteria,
  SortParams,
} from "../../interfaces";
import { purchaseOrderService } from "../../services";

const AllPurchaseOrderTab: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const filter: PurchaseOrderFilterCriteria = {
    query: searchParams.get("query") || undefined,
    status: searchParams.get("status") || undefined,
  };

  const { data, isLoading } = useQuery({
    queryKey: ["purchase-orders", pagination, sort, filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => purchaseOrderService.getPage(pagination, sort, filter),
  });

  const handleSearch: SearchProps["onSearch"] = (value) => {
    if (value) {
      searchParams.set("query", value);
    } else {
      searchParams.delete("query");
    }
    setSearchParams(searchParams);
  };
  return (
    <>
      <div className="mb-5 flex items-center justify-between">
        <div className="w-[40%]">
          <Search
            placeholder="Nhập mã đơn đặt hàng hoặc tên nhà cung cấp"
            defaultValue={searchParams.get("query") || ""}
            enterButton
            allowClear
            onSearch={handleSearch}
          />
        </div>
      </div>
      <PurchaseOrderTable
        purchaseOrderPage={data?.payload}
        isLoading={isLoading}
      />
    </>
  );
};

export default AllPurchaseOrderTab;
