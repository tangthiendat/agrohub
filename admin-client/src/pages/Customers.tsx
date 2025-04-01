import Access from "../features/auth/Access";
import { PERMISSIONS } from "../common/constants";
import { useTitle } from "../common/hooks";
import { Module } from "../common/enums";
import AddCustomer from "../features/customer/AddCustomer";

const Customers: React.FC = () => {
  useTitle("Khách hàng");
  return (
    <Access permission={PERMISSIONS[Module.CUSTOMER].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Khách hàng</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex gap-3">
              {/* <Search
                placeholder="Nhập ID, tên nhà cung cấp, email hoặc số điện thoại"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              /> */}
            </div>
          </div>

          <Access permission={PERMISSIONS[Module.CUSTOMER].CREATE} hideChildren>
            <AddCustomer />
          </Access>
        </div>
        {/* <SupplierTable supplierPage={data?.payload} isLoading={isLoading} /> */}
      </div>
    </Access>
  );
};

export default Customers;
