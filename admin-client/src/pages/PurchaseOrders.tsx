import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import Access from "../features/auth/Access";
import AllPurchaseOrderTab from "../features/purchase/purchase-order/AllPurchaseOrderTab";

const PurchaseOrders: React.FC = () => {
  useTitle("Đơn đặt hàng nhà cung cấp");

  return (
    <Access permission={PERMISSIONS[Module.PURCHASE_ORDER].GET_PAGE}>
      <div className="card">
        <div className="mb-2 flex items-center justify-between">
          <h2 className="text-xl font-semibold">Đơn đặt hàng nhà cung cấp</h2>
        </div>
        <AllPurchaseOrderTab />
      </div>
    </Access>
  );
};

export default PurchaseOrders;
