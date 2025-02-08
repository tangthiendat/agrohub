import Access from "../features/auth/Access";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import AddUnit from "../features/unit/AddUnit";

const Units: React.FC = () => {
  useTitle("Đơn vị tính");
  return (
    <Access permission={PERMISSIONS[Module.UNIT].GET_PAGE}>
      <div className="card">
        <div className="mb-5 flex items-center justify-between">
          <h2 className="text-xl font-semibold">Đơn vị tính</h2>
          <Access permission={PERMISSIONS[Module.UNIT].CREATE} hideChildren>
            <AddUnit />
          </Access>
        </div>
        {/* <CategoryTable /> */}
      </div>
    </Access>
  );
};

export default Units;
