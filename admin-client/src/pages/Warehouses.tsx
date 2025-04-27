import Access from "../features/auth/Access";
import AddWarehouse from "../features/item/warehouse/AddWarehouse";
import { Module } from "../common/enums";
import { PERMISSIONS } from "../common/constants";
import WarehouseTable from "../features/item/warehouse/WarehouseTable";

const Warehouses: React.FC = () => {
  return (
    <Access permission={PERMISSIONS[Module.WAREHOUSE].GET_PAGE}>
      <div className="card">
        <div className="mb-5 flex items-center justify-between">
          <h2 className="mb-3 text-xl font-semibold">Kho hàng</h2>

          <Access
            permission={PERMISSIONS[Module.WAREHOUSE].CREATE}
            hideChildren
          >
            <AddWarehouse />
          </Access>
        </div>
        <WarehouseTable />
      </div>
    </Access>
  );
};

export default Warehouses;
