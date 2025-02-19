import { useTitle } from "../common/hooks";
import Access from "../features/auth/Access";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import AddSupplier from "../features/supplier/AddSupplier";

const Suppliers: React.FC = () => {
  useTitle("Nhà cung cấp");
  // const [searchParams, setSearchParams] = useSearchParams();

  // const pagination: PaginationParams = {
  //   page: Number(searchParams.get("page")) || 1,
  //   pageSize: Number(searchParams.get("pageSize")) || 10,
  // };

  // const sort: SortParams = {
  //   sortBy: searchParams.get("sortBy") || "",
  //   direction: searchParams.get("direction") || "",
  // };

  // const filter: SupplierFilterCriteria = {
  //   query: searchParams.get("query") || undefined,
  //   categoryId: Number(searchParams.get("categoryId")) || undefined,
  // };

  // const { data, isLoading } = useQuery({
  //   queryKey: ["supplier", pagination, sort, filter].filter((key) => {
  //     if (typeof key === "string") {
  //       return key !== "";
  //     } else if (key instanceof Object) {
  //       return Object.values(key).some(
  //         (value) => value !== undefined && value !== "",
  //       );
  //     }
  //   }),
  //   queryFn: () => supplierService.getPage(pagination, sort, filter),
  // });

  // const handleSearch: SearchProps["onSearch"] = (value) => {
  //   if (value) {
  //     searchParams.set("query", value);
  //   } else {
  //     searchParams.delete("query");
  //   }
  //   setSearchParams(searchParams);
  // };

  return (
    <Access permission={PERMISSIONS[Module.SUPPLIER].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Nhà cung cấp</h2>
        <div className="mb-5 flex items-center justify-end">
          {/* <div className="w-[40%]">
            <div className="flex gap-3">
              <Search
                placeholder="Nhập tên hoặc mã sản phẩm"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              />
            </div>
          </div> */}

          <Access permission={PERMISSIONS[Module.SUPPLIER].CREATE} hideChildren>
            <AddSupplier />
          </Access>
        </div>
        {/* <ProductTable productPage={data?.payload} isLoading={isLoading} /> */}
      </div>
    </Access>
  );
};

export default Suppliers;
