import { useNavigate } from "react-router";
import { Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import Access from "../features/auth/Access";
import { PERMISSIONS } from "../common/constants";
import { useTitle } from "../common/hooks";
import { Module } from "../common/enums";

const PurchaseOrders: React.FC = () => {
  useTitle("Đơn đặt hàng nhà cung cấp");
  const navigate = useNavigate();
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
        {/* <UnitTable /> */}
      </div>
    </Access>
  );
};

export default PurchaseOrders;
