import { PlusOutlined } from "@ant-design/icons";
import { Button } from "antd";
import { useNavigate } from "react-router";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import Access from "../features/auth/Access";
import AllPurchaseOrderTab from "../features/purchase-order/AllPurchaseOrderTab";

const PurchaseOrders: React.FC = () => {
  useTitle("Đơn đặt hàng nhà cung cấp");
  const navigate = useNavigate();

  // function handleTabChange(key: string) {
  //   if (key !== "all") {
  //     //reset all query params
  //     if (searchParams.has("query")) {
  //       searchParams.delete("query");
  //     }
  //     if (searchParams.has("status")) {
  //       searchParams.delete("status");
  //     }
  //     if (searchParams.has("page")) {
  //       searchParams.delete("page");
  //     }
  //     if (searchParams.has("pageSize")) {
  //       searchParams.delete("pageSize");
  //     }
  //     setSearchParams(searchParams);
  //   }
  // }

  // const items: TabsProps["items"] = [
  //   {
  //     key: "all",
  //     label: "Tất cả",
  //     children: <AllPurchaseOrderTab />,
  //   },
  //   {
  //     key: "pending",
  //     label: "Chờ xác nhận",
  //     children: <PendingOrderList />,
  //   },
  //   {
  //     key: "approved",
  //     label: "Đã xác nhận",
  //     children: <ApprovedOrderList />,
  //   },
  // ];

  return (
    <Access permission={PERMISSIONS[Module.PURCHASE_ORDER].GET_PAGE}>
      <div className="card">
        <div className="mb-2 flex items-center justify-between">
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
        <AllPurchaseOrderTab />
      </div>
    </Access>
  );
};

export default PurchaseOrders;
