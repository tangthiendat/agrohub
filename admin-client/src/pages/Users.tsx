import { useQuery } from "@tanstack/react-query";
import { SearchProps } from "antd/es/input";
import Search from "antd/es/input/Search";
import { useSearchParams } from "react-router";
import Access from "../features/auth/Access";
import AddUser from "../features/auth/users/AddUser";
import UserTable from "../features/auth/users/UserTable";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import {
  PaginationParams,
  SortParams,
  UserFilterCriteria,
} from "../interfaces";
import { userService } from "../services";

const Users: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const filter: UserFilterCriteria = {
    gender: searchParams.get("gender") || undefined,
    query: searchParams.get("query") || undefined,
    active:
      searchParams.get("active") == "true"
        ? true
        : searchParams.get("active") == "false"
          ? false
          : undefined,
    roleId: searchParams.get("roleId") || undefined,
  };

  const { data, isLoading } = useQuery({
    queryKey: ["users", pagination, sort, filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => userService.getPage(pagination, sort, filter),
  });

  const handleSearch: SearchProps["onSearch"] = (value) => {
    if (value) {
      searchParams.set("query", value);
    } else {
      searchParams.delete("query");
    }
    setSearchParams(searchParams);
  };

  useTitle("Người dùng");
  return (
    <Access permission={PERMISSIONS[Module.USER].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Người dùng</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex gap-3">
              <Search
                placeholder="Nhập tên hoặc mã sản phẩm"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              />
            </div>
          </div>

          <Access permission={PERMISSIONS[Module.USER].CREATE} hideChildren>
            <AddUser />
          </Access>
        </div>
        <UserTable userPage={data?.payload} isLoading={isLoading} />
      </div>
    </Access>
  );
};

export default Users;
