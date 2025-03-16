import Access from "../features/auth/Access";
import { Module } from "../common/enums";
import { PERMISSIONS } from "../common/constants";
import AddProductLocation from "../features/inventory/AddProductLocation";

const ProductLocations: React.FC = () => {
  return (
    <Access permission={PERMISSIONS[Module.PRODUCT_LOCATION].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Vị trí hàng hóa </h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            {/* <div className="flex gap-3">
              <Search
                placeholder="Nhập ID, tên nhà cung cấp, email hoặc số điện thoại"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              />
            </div> */}
          </div>

          <Access
            permission={PERMISSIONS[Module.PRODUCT_LOCATION].CREATE}
            hideChildren
          >
            <AddProductLocation />
          </Access>
        </div>
        {/* <SupplierTable supplierPage={data?.payload} isLoading={isLoading} /> */}
      </div>
    </Access>
  );
};

export default ProductLocations;
