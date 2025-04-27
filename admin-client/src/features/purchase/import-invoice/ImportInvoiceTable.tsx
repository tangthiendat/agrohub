import { Table, TablePaginationConfig } from "antd";
import { TableProps } from "antd/lib";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import { IImportInvoice, ISupplier, Page } from "../../../interfaces";
import { formatDate } from "../../../utils/datetime";
import { getSortDirection } from "../../../utils/filter";
import { formatCurrency } from "../../../utils/number";
import ViewImportInvoice from "./ViewImportInvoice";

interface ImportInvoiceTableProps {
  importInvoicePage?: Page<IImportInvoice>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const ImportInvoiceTable: React.FC<ImportInvoiceTableProps> = ({
  importInvoicePage,
  isLoading,
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} phiếu nhập kho`,
    },
  }));

  useEffect(() => {
    if (importInvoicePage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: importInvoicePage.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} phiếu nhập kho`,
        },
      }));
    }
  }, [importInvoicePage]);

  const handleTableChange: TableProps<IImportInvoice>["onChange"] = (
    pagination,
    filters,
    sorter,
  ) => {
    setTableParams((prev) => ({
      ...prev,
      pagination,
      sorter,
      filters,
    }));

    searchParams.set("page", String(pagination.current));
    searchParams.set("pageSize", String(pagination.pageSize));

    if (filters) {
      Object.entries(filters).forEach(([key, value]) => {
        if (!value) {
          searchParams.delete(key);
          return;
        }
        const paramValue = Array.isArray(value)
          ? value.join(",")
          : String(value);
        searchParams.set(key, paramValue);
      });
    }

    let sortBy;
    let direction;

    if (sorter) {
      if (Array.isArray(sorter)) {
        sortBy = sorter[0].field as string;
        direction = getSortDirection(sorter[0].order as string);
      } else {
        sortBy = sorter.field as string;
        direction = getSortDirection(sorter.order as string);
      }
    }

    if (sortBy && direction) {
      searchParams.set("sortBy", sortBy);
      searchParams.set("direction", direction);
    } else {
      searchParams.delete("direction");
      searchParams.delete("sortBy");
    }

    setSearchParams(searchParams);
  };

  const columns: TableProps<IImportInvoice>["columns"] = [
    {
      title: "Mã phiếu thu",
      dataIndex: "importInvoiceId",
      key: "importInvoiceId",
      width: "10%",
    },
    {
      title: "Nhà cung cấp",
      dataIndex: "supplier",
      key: "supplier",
      width: "20%",
      render: (supplier: ISupplier) => supplier.supplierName,
    },
    {
      title: "Kho",
      dataIndex: "warehouse",
      key: "warehouse",
      width: "10%",
      render: (warehouse) => warehouse.warehouseName,
    },
    {
      title: "Ngày lập phiếu",
      dataIndex: "createdDate",
      key: "createdDate",
      width: "15%",
      render: (createdDate: string) => formatDate(createdDate),
    },
    {
      title: "Người lập phiếu",
      dataIndex: "user",
      key: "user",
      width: "15%",
      render: (user) => user.fullName,
    },
    {
      title: "Tổng tiền",
      dataIndex: "finalAmount",
      key: "finalAmount",
      width: "15%",
      align: "right",
      render: (finalAmount) => formatCurrency(finalAmount),
    },
    {
      title: "Hành động",
      key: "action",
      width: "15%",
      render: (_, record: IImportInvoice) => (
        <ViewImportInvoice importInvoice={record} />
      ),
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: IImportInvoice) => record.importInvoiceId}
      pagination={tableParams.pagination}
      dataSource={importInvoicePage?.content}
      rowClassName={(_, index) =>
        index % 2 === 0 ? "table-row-light" : "table-row-gray"
      }
      rowHoverable={false}
      loading={{
        spinning: isLoading,
        tip: "Đang tải dữ liệu...",
      }}
      onChange={handleTableChange}
      size="middle"
    />
  );
};

export default ImportInvoiceTable;
