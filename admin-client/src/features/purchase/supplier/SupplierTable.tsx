import {
  CaretDownFilled,
  CaretUpFilled,
  FilterFilled,
} from "@ant-design/icons";
import { Space, Table, TablePaginationConfig, TableProps, Tag } from "antd";
import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router";
import MoneyIcon from "../../../common/components/icons/MoneyIcon";
import { PERMISSIONS } from "../../../common/constants";
import { Module } from "../../../common/enums";
import { ISupplier, Page } from "../../../interfaces";
import {
  getFilterIconColor,
  getSortDownIconColor,
  getSortUpIconColor,
} from "../../../utils/color";
import { formatTimestamp } from "../../../utils/datetime";
import {
  getDefaultFilterValue,
  getDefaultSortOrder,
  getSortDirection,
} from "../../../utils/filter";
import Access from "../../auth/Access";
import UpdateSupplier from "./UpdateSupplier";
import UpdateSupplierStatus from "./UpdateSupplierStatus";
import ViewSupplier from "./ViewSupplier";

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
  const navigate = useNavigate();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} nhà cung cấp`,
    },
  }));

  useEffect(() => {
    if (supplierPage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: supplierPage.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} nhà cung cấp`,
        },
      }));
    }
  }, [supplierPage]);

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
      title: "Điểm tín nhiệm",
      key: "trustScore",
      width: "12%",
      render: (_, supplier: ISupplier) =>
        supplier.supplierRating && `${supplier.supplierRating.trustScore}/100`,
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
          <Access permission={PERMISSIONS[Module.SUPPLIER].UPDATE} hideChildren>
            <UpdateSupplier supplier={supplier} />
          </Access>
          {/* <Access permission={PERMISSIONS[Module.SUPPLIER].RATE} hideChildren>
            <RateSupplier supplier={supplier} />
          </Access> */}
          <Access
            permission={PERMISSIONS[Module.DEBT_ACCOUNT].GET_SUPPLIER_PAGE}
            hideChildren
          >
            <MoneyIcon
              tooltipTitle="Công nợ"
              onClick={() => navigate(`${supplier.supplierId}/debt`)}
            />
          </Access>
          <Access
            permission={PERMISSIONS[Module.SUPPLIER].UPDATE_STATUS}
            hideChildren
          >
            <UpdateSupplierStatus supplier={supplier} />
          </Access>
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
