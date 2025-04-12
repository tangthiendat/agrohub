import { Table, TablePaginationConfig } from "antd";
import { TableProps } from "antd/lib";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import { ICustomer, IExportInvoice, Page } from "../../../interfaces";
import { formatDate } from "../../../utils/datetime";
import { getSortDirection } from "../../../utils/filter";
import { formatCurrency } from "../../../utils/number";
import ViewExportInvoice from "./ViewExportInvoice";

interface ImportInvoiceTableProps {
  exportInvoicePage?: Page<IExportInvoice>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const ExportInvoiceTable: React.FC<ImportInvoiceTableProps> = ({
  exportInvoicePage,
  isLoading,
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} phiếu xuất kho`,
    },
  }));

  useEffect(() => {
    if (exportInvoicePage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: exportInvoicePage.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} phiếu nhập kho`,
        },
      }));
    }
  }, [exportInvoicePage]);

  const handleTableChange: TableProps<IExportInvoice>["onChange"] = (
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

  const columns: TableProps<IExportInvoice>["columns"] = [
    {
      title: "Mã phiếu xuất",
      dataIndex: "exportInvoiceId",
      key: "exportInvoiceId",
      width: "10%",
    },
    {
      title: "Khách hàng",
      dataIndex: "customer",
      key: "customer",
      width: "20%",
      render: (customer: ICustomer) => customer.customerName,
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
      render: (_, record: IExportInvoice) => (
        <ViewExportInvoice exportInvoice={record} />
      ),
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: IExportInvoice) => record.exportInvoiceId}
      pagination={tableParams.pagination}
      dataSource={exportInvoicePage?.content}
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

export default ExportInvoiceTable;
