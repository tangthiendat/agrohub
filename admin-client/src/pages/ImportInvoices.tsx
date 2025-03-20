import { Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useNavigate, useSearchParams } from "react-router";
import Access from "../features/auth/Access";
import { useTitle } from "../common/hooks";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import {
  ImportInvoiceFilterCriteria,
  PaginationParams,
  SortParams,
} from "../interfaces";
import { importInvoiceService } from "../services";
import { useQuery } from "@tanstack/react-query";
import ImportInvoiceTable from "../features/import-invoice/ImportInvoiceTable";

const ImportInvoices: React.FC = () => {
  useTitle("Phiếu nhập kho");
  const navigate = useNavigate();

  const [searchParams, setSearchParams] = useSearchParams();

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  // const filter: ImportInvoiceFilterCriteria = {
  //   query: searchParams.get("query") || undefined,
  // };

  const { data, isLoading } = useQuery({
    queryKey: ["import-invoices", pagination, sort].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => importInvoiceService.getPage(pagination, sort),
  });

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
        <ImportInvoiceTable
          importInvoicePage={data?.payload}
          isLoading={isLoading}
        />
      </div>
    </Access>
  );
};

export default ImportInvoices;
