import { useNavigate, useSearchParams } from "react-router";
import { PlusOutlined } from "@ant-design/icons";
import { useTitle } from "../common/hooks";
import Access from "../features/auth/Access";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import Search from "antd/es/input/Search";
import { Button } from "antd";

const Products: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();
  useTitle("Người dùng");
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
                // onSearch={handleSearch}
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
        {/* <UserTable userPage={data?.payload} isLoading={isLoading} /> */}
      </div>
    </Access>
  );
};

export default Products;
