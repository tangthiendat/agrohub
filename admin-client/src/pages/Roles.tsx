import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import Access from "../features/auth/Access";
import RoleTable from "../features/auth/roles/RoleTable";

const Roles: React.FC = () => {
  useTitle("Vai trò");
  return (
    <Access permission={PERMISSIONS[Module.ROLE].GET_PAGE}>
      <div className="card">
        <div className="mb-5 flex items-center justify-between">
          <h2 className="text-xl font-semibold">Vai trò</h2>
          {/* <Access
            permission={PERMISSIONS[Module.PERMISSION].CREATE}
            hideChildren
          >
          </Access> */}
        </div>
        <RoleTable />
      </div>
    </Access>
  );
};

export default Roles;
