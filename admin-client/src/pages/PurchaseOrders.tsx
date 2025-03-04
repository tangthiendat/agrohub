import { PlusOutlined } from "@ant-design/icons";
import { Button, Tabs, TabsProps } from "antd";
import { useNavigate, useSearchParams } from "react-router";
import Access from "../features/auth/Access";
import AllPurchaseOrderTab from "../features/purchase-order/AllPurchaseOrderTab";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import PendingOrderList from "../features/purchase-order/PendingOrderList";

const PurchaseOrders: React.FC = () => {
  useTitle("Đơn đặt hàng nhà cung cấp");
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();

  function handleTabChange(key: string) {
    if (key !== "all") {
      //reset all query params
      if (searchParams.has("query")) {
        searchParams.delete("query");
      }
      if (searchParams.has("status")) {
        searchParams.delete("status");
      }
      if (searchParams.has("page")) {
        searchParams.delete("page");
      }
      if (searchParams.has("pageSize")) {
        searchParams.delete("pageSize");
      }
      setSearchParams(searchParams);
    }
  }

  const items: TabsProps["items"] = [
    {
      key: "all",
      label: "Tất cả",
      children: <AllPurchaseOrderTab />,
    },
    {
      key: "pending",
      label: "Chờ xác nhận",
      children: <PendingOrderList />,
    },
    {
      key: "approved",
      label: "Đã xác nhận",
      children: "Đã xác nhận",
    },
  ];

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
        <Tabs items={items} defaultActiveKey="all" onChange={handleTabChange} />
      </div>
    </Access>
  );
};

export default PurchaseOrders;
