import Access from "../features/auth/Access";
import UserTable from "../features/auth/users/UserTable";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import AddUser from "../features/auth/users/AddUser";

const Users: React.FC = () => {
  useTitle("Người dùng");
  return (
    <Access permission={PERMISSIONS[Module.USER].GET_PAGE}>
      <div className="card">
        <div className="mb-5 flex items-center justify-between">
          <h2 className="text-xl font-semibold">Người dùng</h2>
          <Access permission={PERMISSIONS[Module.USER].CREATE} hideChildren>
            <AddUser />
          </Access>
        </div>
        <UserTable />
      </div>
    </Access>
  );
};

export default Users;
