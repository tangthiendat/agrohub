import { Button } from "antd";
import { SearchProps } from "antd/es/input";
import Search from "antd/es/input/Search";
import { PlusOutlined } from "@ant-design/icons";
import { useNavigate, useSearchParams } from "react-router";
import { useQuery } from "@tanstack/react-query";
import Access from "../features/auth/Access";
import ImportInvoiceTable from "../features/import-invoice/ImportInvoiceTable";
import { useTitle } from "../common/hooks";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import {
  ImportInvoiceFilterCriteria,
  PaginationParams,
  SortParams,
} from "../interfaces";
import { importInvoiceService } from "../services";

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

  const filter: ImportInvoiceFilterCriteria = {
    query: searchParams.get("query") || undefined,
  };

  const { data, isLoading } = useQuery({
    queryKey: ["import-invoices", pagination, sort, filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => importInvoiceService.getPage(pagination, sort, filter),
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
    <Access permission={PERMISSIONS[Module.IMPORT_INVOICE].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Phiếu nhập kho</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex justify-between gap-3">
              <Search
                placeholder="Tìm kiếm theo mã phiếu nhập hoặc tên nhà cung cấp"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              />
            </div>
          </div>
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
