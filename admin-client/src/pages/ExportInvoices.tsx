import { useNavigate } from "react-router";
import { Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import Access from "../features/auth/Access";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";

const ExportInvoices: React.FC = () => {
  useTitle("Phiếu xuất kho");
  const navigate = useNavigate();
  return (
    <Access permission={PERMISSIONS[Module.EXPORT_INVOICE].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Phiếu xuất kho kho</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex justify-between gap-3">
              {/* <Search
                placeholder="Tìm kiếm theo mã phiếu nhập hoặc tên nhà cung cấp"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              /> */}
            </div>
          </div>
          <Access
            permission={PERMISSIONS[Module.EXPORT_INVOICE].CREATE}
            hideChildren
          >
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => navigate("new")}
            >
              Thêm mới
            </Button>
          </Access>
        </div>
        {/* <ImportInvoiceTable
          importInvoicePage={data?.payload}
          isLoading={isLoading}
        /> */}
      </div>
    </Access>
  );
};

export default ExportInvoices;
