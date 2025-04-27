import { useQuery } from "@tanstack/react-query";
import { SearchProps } from "antd/es/input";
import Search from "antd/es/input/Search";
import { useSearchParams } from "react-router";
import Access from "../features/auth/Access";
import AddProductLocation from "../features/inventory/location/AddProductLocation";
import ProductLocationTable from "../features/inventory/location/ProductLocationTable";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import {
  PaginationParams,
  ProductLocationFilterCriteria,
  SortParams,
} from "../interfaces";
import { productLocationService } from "../services";

const ProductLocations: React.FC = () => {
  useTitle("Vị trí hàng hóa ");
  const [searchParams, setSearchParams] = useSearchParams();

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const filter: ProductLocationFilterCriteria = {
    query: searchParams.get("query") || undefined,
    rackType: searchParams.get("rackType") || undefined,
    status: searchParams.get("status") || undefined,
  };

  const { data, isLoading } = useQuery({
    queryKey: ["product-locations", pagination, sort, filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => productLocationService.getPage(pagination, sort, filter),
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
    <Access permission={PERMISSIONS[Module.PRODUCT_LOCATION].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Vị trí hàng hóa </h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex gap-3">
              <Search
                placeholder="Nhập tên vị trí (ví dụ A1.2)"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              />
            </div>
          </div>

          <Access
            permission={PERMISSIONS[Module.PRODUCT_LOCATION].CREATE}
            hideChildren
          >
            <AddProductLocation />
          </Access>
        </div>
        <ProductLocationTable
          productLocationPage={data?.payload}
          isLoading={isLoading}
        />
      </div>
    </Access>
  );
};

export default ProductLocations;
