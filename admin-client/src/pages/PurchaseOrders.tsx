import { useNavigate, useSearchParams } from "react-router";
import { Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import Access from "../features/auth/Access";
import { PERMISSIONS } from "../common/constants";
import { useTitle } from "../common/hooks";
import { Module } from "../common/enums";
import { PaginationParams, SortParams } from "../interfaces";
import { useQuery } from "@tanstack/react-query";
import { purchaseOrderService } from "../services/purchase/purchase-order-service";
import PurchaseOrderTable from "../features/purchase-order/PurchaseOrderTable";

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

  const { data, isLoading } = useQuery({
    queryKey: ["suppliers", pagination, sort].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => purchaseOrderService.getPage(pagination, sort),
  });

  return (
    <Access permission={PERMISSIONS[Module.PURCHASE_ORDER].GET_PAGE}>
      <div className="card">
        <div className="mb-5 flex items-center justify-between">
          <h2 className="text-xl font-semibold">Đơn đặt hàng nhà cung cấp</h2>
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
