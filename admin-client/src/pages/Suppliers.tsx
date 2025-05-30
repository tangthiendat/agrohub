import { useSearchParams } from "react-router";
import { useQuery } from "@tanstack/react-query";
import AddSupplier from "../features/purchase/supplier/AddSupplier";
import SupplierTable from "../features/purchase/supplier/SupplierTable";
import { useTitle } from "../common/hooks";
import Access from "../features/auth/Access";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import {
  PaginationParams,
  SortParams,
  SupplierFilterCriteria,
} from "../interfaces";
import { supplierService } from "../services";
import { SearchProps } from "antd/es/input";
import Search from "antd/es/input/Search";

const Suppliers: React.FC = () => {
  useTitle("Nhà cung cấp");
  const [searchParams, setSearchParams] = useSearchParams();

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const filter: SupplierFilterCriteria = {
    query: searchParams.get("query") || undefined,
    active:
      searchParams.get("active") == "true"
        ? true
        : searchParams.get("active") == "false"
          ? false
          : undefined,
  };

  const { data, isLoading } = useQuery({
    queryKey: ["suppliers", pagination, sort, filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => supplierService.getPage(pagination, sort, filter),
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
    <Access permission={PERMISSIONS[Module.SUPPLIER].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Nhà cung cấp</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex gap-3">
              <Search
                placeholder="Nhập ID, tên nhà cung cấp, email hoặc số điện thoại"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              />
            </div>
          </div>

          <Access permission={PERMISSIONS[Module.SUPPLIER].CREATE} hideChildren>
            <AddSupplier />
          </Access>
        </div>
        <SupplierTable supplierPage={data?.payload} isLoading={isLoading} />
      </div>
    </Access>
  );
};

export default Suppliers;
