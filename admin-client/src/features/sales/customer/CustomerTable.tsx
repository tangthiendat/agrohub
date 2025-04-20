import { useEffect, useState } from "react";
import { Space, Table, TablePaginationConfig, Tag } from "antd";
import {
  CaretDownFilled,
  CaretUpFilled,
  FilterFilled,
} from "@ant-design/icons";
import { useNavigate, useSearchParams } from "react-router";
import { TableProps } from "antd/lib";
import {
  getDefaultFilterValue,
  getDefaultSortOrder,
  getSortDirection,
} from "../../../utils/filter";
import { ICustomer, Page } from "../../../interfaces";
import { formatTimestamp } from "../../../utils/datetime";
import {
  getFilterIconColor,
  getSortDownIconColor,
  getSortUpIconColor,
} from "../../../utils/color";
import ViewCustomer from "./ViewCustomer";
import { CustomerType, Module } from "../../../common/enums";
import { CUSTOMER_TYPE_NAME, PERMISSIONS } from "../../../common/constants";
import UpdateCustomer from "./UpdateCustomer";
import Access from "../../auth/Access";
import UpdateCustomerStatus from "./UpdateCustomerStatus";
import MoneyIcon from "../../../common/components/icons/MoneyIcon";

interface CustomerTableProps {
  customerPage?: Page<ICustomer>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const CustomerTable: React.FC<CustomerTableProps> = ({
  customerPage,
  isLoading,
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} khách hàng`,
    },
  }));

  useEffect(() => {
    if (customerPage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: customerPage.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} khách hàng`,
        },
      }));
    }
  }, [customerPage]);

  const handleTableChange: TableProps<ICustomer>["onChange"] = (
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

  const columns: TableProps<ICustomer>["columns"] = [
    {
      title: "ID",
      dataIndex: "customerId",
      key: "customerId",
      width: "8%",
    },
    {
      title: "Tên khách hàng",
      dataIndex: "customerName",
      key: "customerName",
      width: "16%",
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
      filters: [
        {
          text: "Đã kích hoạt",
          value: true,
        },
        {
          text: "Chưa kích hoạt",
          value: false,
        },
      ],
      defaultFilteredValue: getDefaultFilterValue(searchParams, "active"),
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },

    {
      title: "Loại khách hàng",
      dataIndex: "customerType",
      key: "customerType",
      width: "12%",
      render: (customerType: CustomerType) => CUSTOMER_TYPE_NAME[customerType],
      filters: Object.values(CustomerType).map((customerType: string) => ({
        text: CUSTOMER_TYPE_NAME[customerType],
        value: customerType,
      })),
      defaultFilteredValue: getDefaultFilterValue(searchParams, "customerType"),
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
    {
      title: "Số điện thoại",
      dataIndex: "phoneNumber",
      key: "phoneNumber",
      width: "10%",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      width: "18%",
    },
    {
      title: "Ngày cập nhật",
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
      render: (_, customer: ICustomer) => (
        <Space>
          <ViewCustomer customer={customer} />
          <Access permission={PERMISSIONS[Module.CUSTOMER].UPDATE} hideChildren>
            <UpdateCustomer customer={customer} />
          </Access>
          <MoneyIcon
            tooltipTitle="Công nợ"
            onClick={() => navigate(`${customer.customerId}/debt`)}
          />
          <Access
            permission={PERMISSIONS[Module.CUSTOMER].UPDATE_STATUS}
            hideChildren
          >
            <UpdateCustomerStatus customer={customer} />
          </Access>
        </Space>
      ),
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey="customerId"
      pagination={tableParams.pagination}
      dataSource={customerPage?.content}
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

export default CustomerTable;
