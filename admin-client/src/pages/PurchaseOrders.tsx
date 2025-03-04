import { useNavigate, useSearchParams } from "react-router";
import { Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { SearchProps } from "antd/es/input";
import { useQuery } from "@tanstack/react-query";
import Access from "../features/auth/Access";
import Search from "antd/es/input/Search";
import PurchaseOrderTable from "../features/purchase-order/PurchaseOrderTable";
import { PERMISSIONS } from "../common/constants";
import { useTitle } from "../common/hooks";
import { Module } from "../common/enums";
import {
  PaginationParams,
  PurchaseOrderFilterCriteria,
  SortParams,
} from "../interfaces";
import { purchaseOrderService } from "../services";

const PurchaseOrders: React.FC = () => {
  useTitle("Đơn đặt hàng nhà cung cấp");
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

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
    queryKey: ["suppliers", pagination, sort, filter].filter((key) => {
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
    <Access permission={PERMISSIONS[Module.PURCHASE_ORDER].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">
          Đơn đặt hàng nhà cung cấp
        </h2>
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
          <Access
            permission={PERMISSIONS[Module.PURCHASE_ORDER].CREATE}
            hideChildren
          >
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => navigate("new")}
            >
              Thêm mới
            </Button>
          </Access>
        </div>
        <PurchaseOrderTable
          purchaseOrderPage={data?.payload}
          isLoading={isLoading}
        />
      </div>
    </Access>
  );
};

export default PurchaseOrders;
