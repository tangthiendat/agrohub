import { useNavigate, useSearchParams } from "react-router";
import { PlusOutlined } from "@ant-design/icons";
import Search from "antd/es/input/Search";
import { Button } from "antd";
import Access from "../features/auth/Access";
import { useTitle } from "../common/hooks";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import {
  PaginationParams,
  ProductFilterCriteria,
  SortParams,
} from "../interfaces";
import { useQuery } from "@tanstack/react-query";
import { productService, userService } from "../services";
import ProductTable from "../features/product/ProductTable";
import { SearchProps } from "antd/lib/input";

const Products: React.FC = () => {
  useTitle("Sản phẩm");
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const filter: ProductFilterCriteria = {
    query: searchParams.get("query") || undefined,
    categoryId: Number(searchParams.get("categoryId")) || undefined,
  };

  const { data, isLoading } = useQuery({
    queryKey: ["products", pagination, sort, filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => productService.getPage(pagination, sort, filter),
  });

  const handleSearch: SearchProps["onSearch"] = (value) => {
    if (value) {
      searchParams.set("query", value);
    } else {
      searchParams.delete("query");
    }
    setSearchParams(searchParams);
  };

  return (
    <Access permission={PERMISSIONS[Module.PRODUCT].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Sản phẩm</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex gap-3">
              <Search
                placeholder="Nhập tên hoặc mã sản phẩm"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              />
            </div>
          </div>

          <Access permission={PERMISSIONS[Module.PRODUCT].CREATE} hideChildren>
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => navigate("new")}
            >
              Thêm mới
            </Button>
          </Access>
        </div>
        <ProductTable productPage={data?.payload} isLoading={isLoading} />
      </div>
    </Access>
  );
};

export default Products;
