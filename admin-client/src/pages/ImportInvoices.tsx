import { Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router";
import Access from "../features/auth/Access";
import { useTitle } from "../common/hooks";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";

const ImportInvoices: React.FC = () => {
  useTitle("Phiếu nhập kho");
  const navigate = useNavigate();

  return (
    <Access permission={PERMISSIONS[Module.IMPORT_INVOICE].GET_PAGE}>
      <div className="card">
        <div className="mb-2 flex items-center justify-between">
          <h2 className="text-xl font-semibold">Phiếu nhập kho</h2>
          <Access
            permission={PERMISSIONS[Module.IMPORT_INVOICE].CREATE}
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
        {/* <AllPurchaseOrderTab /> */}
      </div>
    </Access>
  );
};

export default ImportInvoices;
