import Access from "../features/auth/Access";
import { PERMISSIONS } from "../common/constants";
import { useTitle } from "../common/hooks";
import { Module } from "../common/enums";
import AddCustomer from "../features/sales/customer/AddCustomer";
import { useSearchParams } from "react-router";
import {
  CustomerFilterCriteria,
  PaginationParams,
  SortParams,
} from "../interfaces";
import { useQuery } from "@tanstack/react-query";
import { customerService } from "../services";
import CustomerTable from "../features/sales/customer/CustomerTable";
import { SearchProps } from "antd/es/input";
import Search from "antd/es/input/Search";

const Customers: React.FC = () => {
  useTitle("Khách hàng");

  const [searchParams, setSearchParams] = useSearchParams();

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const filter: CustomerFilterCriteria = {
    query: searchParams.get("query") || undefined,
    active:
      searchParams.get("active") == "true"
        ? true
        : searchParams.get("active") == "false"
          ? false
          : undefined,
    customerType: searchParams.get("customerType") || undefined,
  };

  const { data, isLoading } = useQuery({
    queryKey: ["customers", pagination, sort, filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => customerService.getPage(pagination, sort, filter),
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
    <Access permission={PERMISSIONS[Module.CUSTOMER].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Khách hàng</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex gap-3">
              <Search
                placeholder="Nhập ID, tên khách hàng, email hoặc số điện thoại"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              />
            </div>
          </div>

          <Access permission={PERMISSIONS[Module.CUSTOMER].CREATE} hideChildren>
            <AddCustomer />
          </Access>
        </div>
        <CustomerTable customerPage={data?.payload} isLoading={isLoading} />
      </div>
    </Access>
  );
};

export default Customers;
