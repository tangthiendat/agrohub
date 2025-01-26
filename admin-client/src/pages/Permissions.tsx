import Access from "../features/auth/Access";
import AddPermission from "../features/auth/permissions/AddPermission";
import PermissionTable from "../features/auth/permissions/PermissionTable";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";

const Permissions: React.FC = () => {
  useTitle("Quyền hạn");
  return (
    <Access permission={PERMISSIONS[Module.PERMISSION].GET_PAGE}>
      <div className="card">
        <div className="mb-5 flex items-center justify-between">
          <h2 className="text-xl font-semibold">Quyền hạn</h2>
          <Access
            permission={PERMISSIONS[Module.PERMISSION].CREATE}
            hideChildren
          >
            <AddPermission />
          </Access>
        </div>
        <PermissionTable />
      </div>
    </Access>
  );
};

export default Permissions;
