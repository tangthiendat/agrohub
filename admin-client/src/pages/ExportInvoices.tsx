import { useNavigate, useSearchParams } from "react-router";
import { Button } from "antd";
import { useQuery } from "@tanstack/react-query";
import { PlusOutlined } from "@ant-design/icons";
import Access from "../features/auth/Access";
import Search from "antd/es/input/Search";
import { SearchProps } from "antd/es/input";
import { PERMISSIONS } from "../common/constants";
import { Module } from "../common/enums";
import { useTitle } from "../common/hooks";
import {
  ExportInvoiceFilterCriteria,
  PaginationParams,
  SortParams,
} from "../interfaces";
import { exportInvoiceService } from "../services";
import ExportInvoiceTable from "../features/sales/export-invoice/ExportInvoiceTable";

const ExportInvoices: React.FC = () => {
  useTitle("Phiếu xuất kho");
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

  const filter: ExportInvoiceFilterCriteria = {
    query: searchParams.get("query") || undefined,
  };

  const { data, isLoading } = useQuery({
    queryKey: ["export-invoices", pagination, sort, filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => exportInvoiceService.getPage(pagination, sort, filter),
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
    <Access permission={PERMISSIONS[Module.EXPORT_INVOICE].GET_PAGE}>
      <div className="card">
        <h2 className="mb-3 text-xl font-semibold">Phiếu xuất kho</h2>
        <div className="mb-5 flex items-center justify-between">
          <div className="w-[40%]">
            <div className="flex justify-between gap-3">
              <Search
                placeholder="Tìm kiếm theo mã phiếu xuất kho hoặc tên khách hàng"
                defaultValue={searchParams.get("query") || ""}
                enterButton
                allowClear
                onSearch={handleSearch}
              />
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
        <ExportInvoiceTable
          exportInvoicePage={data?.payload}
          isLoading={isLoading}
        />
      </div>
    </Access>
  );
};

export default ExportInvoices;
