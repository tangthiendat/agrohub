import Access from "../features/auth/Access";
import AddRole from "../features/auth/roles/AddRole";
import RoleTable from "../features/auth/roles/RoleTable";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";

const Roles: React.FC = () => {
  useTitle("Vai trò");
  return (
    <Access permission={PERMISSIONS[Module.ROLE].GET_PAGE}>
      <div className="card">
        <div className="mb-5 flex items-center justify-between">
          <h2 className="text-xl font-semibold">Vai trò</h2>
          <Access permission={PERMISSIONS[Module.ROLE].CREATE} hideChildren>
            <AddRole />
          </Access>
        </div>
        <RoleTable />
      </div>
    </Access>
  );
};

export default Roles;
