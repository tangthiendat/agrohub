import Access from "../features/auth/Access";
import AddCategory from "../features/category/AddCategory";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";

const Categories = () => {
  useTitle("Loại sản phẩm");
  return (
    <Access permission={PERMISSIONS[Module.CATEGORY].GET_PAGE}>
      <div className="card">
        <div className="mb-5 flex items-center justify-between">
          <h2 className="text-xl font-semibold">Loại sản phẩm</h2>
          <Access permission={PERMISSIONS[Module.CATEGORY].CREATE} hideChildren>
            <AddCategory />
          </Access>
        </div>
        {/* <PermissionTable /> */}
      </div>
    </Access>
  );
};

export default Categories;
