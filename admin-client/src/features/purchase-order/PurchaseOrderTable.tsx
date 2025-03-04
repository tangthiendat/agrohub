import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import { Table, TablePaginationConfig, Tag } from "antd";
import {
  CaretDownFilled,
  CaretUpFilled,
  FilterFilled,
} from "@ant-design/icons";
import { Page, IPurchaseOrderTableItem } from "../../interfaces";
import {
  getDefaultFilterValue,
  getDefaultSortOrder,
  getSortDirection,
} from "../../utils/filter";
import { TableProps } from "antd/lib";
import { PurchaseOrderStatus } from "../../common/enums";
import { PURCHASE_ORDER_STATUS_COLOR } from "../../common/constants";
import {
  getFilterIconColor,
  getSortDownIconColor,
  getSortUpIconColor,
} from "../../utils/color";

interface PurchaseOrderTableProps {
  purchaseOrderPage?: Page<IPurchaseOrderTableItem>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const PurchaseOrderTable: React.FC<PurchaseOrderTableProps> = ({
  purchaseOrderPage,
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

  useEffect(() => {
    if (purchaseOrderPage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: purchaseOrderPage.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} đơn đặt hàng`,
        },
      }));
    }
  }, [purchaseOrderPage]);

  const handleTableChange: TableProps<IPurchaseOrderTableItem>["onChange"] = (
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

  const columns: TableProps<IPurchaseOrderTableItem>["columns"] = [
    {
      title: "ID",
      dataIndex: "purchaseOrderId",
      key: "purchaseOrderId",
      width: "10%",
    },
    {
      title: "Nhà cung cấp",
      dataIndex: "supplierName",
      key: "supplierName",
      width: "20%",
    },
    {
      title: "Ngày đặt hàng",
      dataIndex: "orderDate",
      key: "orderDate",
      width: "15%",
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "orderDate"),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Ngày nhận hàng dự kiến",
      dataIndex: "expectedDeliveryDate",
      key: "expectedDeliveryDate",
      width: "15%",
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(
        searchParams,
        "expectedDeliveryDate",
      ),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      width: "10%",
      render: (status: PurchaseOrderStatus) => (
        <Tag color={PURCHASE_ORDER_STATUS_COLOR[status]}>{status}</Tag>
      ),
      filters: Object.values(PurchaseOrderStatus).map((status) => ({
        text: status,
        value: status,
      })),
      defaultFilteredValue: getDefaultFilterValue(searchParams, "status"),
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: IPurchaseOrderTableItem) => record.purchaseOrderId}
      pagination={tableParams.pagination}
      dataSource={purchaseOrderPage?.content}
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

export default PurchaseOrderTable;
