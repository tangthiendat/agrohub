import { useState } from "react";
import { Space, Table, TablePaginationConfig, TableProps, Tag } from "antd";
import { CaretDownFilled, CaretUpFilled } from "@ant-design/icons";
import { useSearchParams } from "react-router";
import ViewSupplier from "./ViewSupplier";
import { ISupplier, Page } from "../../interfaces";
import { getDefaultSortOrder, getSortDirection } from "../../utils/filter";
import { formatTimestamp } from "../../utils/datetime";
import { getSortDownIconColor, getSortUpIconColor } from "../../utils/color";

interface SupplierTableProps {
  supplierPage?: Page<ISupplier>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const SupplierTable: React.FC<SupplierTableProps> = ({
  supplierPage,
  isLoading,
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} nhà cung cấp`,
    },
  }));

  const handleTableChange: TableProps<ISupplier>["onChange"] = (
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

  const columns: TableProps<ISupplier>["columns"] = [
    {
      title: "ID",
      dataIndex: "supplierId",
      key: "supplierId",
      width: "8%",
    },
    {
      title: "Tên nhà cung cấp",
      dataIndex: "supplierName",
      key: "supplierName",
      width: "18%",
    },
    {
      title: "Trạng thái",
      dataIndex: "active",
      key: "active",
      width: "10%",
      render: (active: boolean) => (
        <Tag color={active ? "green" : "red"}>
          {active ? "Đã kích hoạt" : "Chưa kích hoạt"}
        </Tag>
      ),
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      width: "16%",
    },
    {
      title: "Số điện thoại",
      dataIndex: "phoneNumber",
      key: "phoneNumber",
      width: "10%",
    },
    {
      title: "Thời gian tạo",
      dataIndex: "createdAt",
      key: "createdAt",
      width: "12%",
      render: (createdAt: string) =>
        createdAt ? formatTimestamp(createdAt) : "",
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "createdAt"),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Thời gian cập nhật",
      dataIndex: "updatedAt",
      key: "updatedAt",
      width: "12%",
      render: (updatedAt: string) =>
        updatedAt ? formatTimestamp(updatedAt) : "",
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "updatedAt"),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Hành động",
      key: "action",
      width: "14%",
      render: (_, supplier) => (
        <Space>
          <ViewSupplier supplier={supplier} />
        </Space>
      ),
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: ISupplier) => record.supplierId}
      pagination={tableParams.pagination}
      dataSource={supplierPage?.content}
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

export default SupplierTable;
