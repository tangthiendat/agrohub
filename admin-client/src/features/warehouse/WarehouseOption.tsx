import { IWarehouse } from "../../interfaces";

interface WarehouseOptionProps {
  warehouse: IWarehouse;
}

const WarehouseOption: React.FC<WarehouseOptionProps> = ({ warehouse }) => {
  return (
    <div>
      <div>{warehouse.warehouseName}</div>
      <div className="text-wrap text-xs text-gray-500">{warehouse.address}</div>
    </div>
  );
};

export default WarehouseOption;
