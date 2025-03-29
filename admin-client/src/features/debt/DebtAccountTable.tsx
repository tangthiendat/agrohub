import { Table, TablePaginationConfig, Tag } from "antd";
import { TableProps } from "antd/lib";
import {
  CaretDownFilled,
  CaretUpFilled,
  FilterFilled,
} from "@ant-design/icons";
import { useSearchParams } from "react-router";
import { useEffect, useState } from "react";
import {
  getDefaultFilterValue,
  getDefaultSortOrder,
  getSortDirection,
} from "../../utils/filter";
import { DebtStatus } from "../../common/enums";
import { formatCurrency } from "../../utils/number";
import { formatDate } from "../../utils/datetime";
import {
  getFilterIconColor,
  getSortDownIconColor,
  getSortUpIconColor,
} from "../../utils/color";
import { DEBT_STATUS_COLOR, DEBT_STATUS_NAME } from "../../common/constants";
import { IPartyDebtAccount, Page } from "../../interfaces";

interface DebtAccountTableProps {
  partyDebtAccountPage?: Page<IPartyDebtAccount>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const DebtAccountTable: React.FC<DebtAccountTableProps> = ({
  partyDebtAccountPage,
  isLoading,
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} khoản nợ`,
    },
  }));

  useEffect(() => {
    if (partyDebtAccountPage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: partyDebtAccountPage.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} khoản nợ`,
        },
      }));
    }
  }, [partyDebtAccountPage]);

  const handleTableChange: TableProps<IPartyDebtAccount>["onChange"] = (
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

  const columns: TableProps<IPartyDebtAccount>["columns"] = [
    {
      title: "STT",
      key: "index",
      width: "5%",
      render: (_, __, index) => <span>{index + 1}</span>,
    },
    {
      title: "Mã phiếu",
      dataIndex: "sourceId",
      key: "sourceId",
      width: "15%",
    },
    {
      title: "Số tiền nợ",
      dataIndex: "totalAmount",
      key: "totalAmount",
      width: "10%",
      align: "right",
      render: (_, record) => formatCurrency(record.totalAmount),
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "totalAmount"),
      sortIcon: ({ sortOrder }) => (
        <div className="ml-1 flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Đã trả",
      dataIndex: "paidAmount",
      key: "paidAmount",
      width: "10%",
      align: "right",
      render: (_, record) => formatCurrency(record.paidAmount),
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "paidAmount"),
      sortIcon: ({ sortOrder }) => (
        <div className="ml-1 flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Còn lại",
      dataIndex: "remainingAmount",
      key: "remainingAmount",
      width: "10%",
      align: "right",
      render: (_, record) => formatCurrency(record.remainingAmount),
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "remainingAmount"),
      sortIcon: ({ sortOrder }) => (
        <div className="ml-1 flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Ngày đến hạn",
      dataIndex: "dueDate",
      key: "dueDate",
      width: "10%",
      render: (dueDate: string) => formatDate(dueDate),
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "dueDate"),
      sortIcon: ({ sortOrder }) => (
        <div className="ml-1 flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Lãi suất",
      dataIndex: "interestRate",
      key: "interestRate",
      width: "10%",
      align: "right",
      render: (_, record) => `${record.interestRate}%`,
    },
    {
      title: "Trạng thái",
      dataIndex: "debtStatus",
      key: "debtStatus",
      width: "15%",
      render: (status: DebtStatus) => (
        <Tag color={DEBT_STATUS_COLOR[status]}>{DEBT_STATUS_NAME[status]}</Tag>
      ),
      filters: Object.values(DebtStatus).map((status) => ({
        text: DEBT_STATUS_NAME[status],
        value: status,
      })),
      defaultFilteredValue: getDefaultFilterValue(searchParams, "debtStatus"),
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
    {
      title: "Hành động",
      key: "action",
      width: "10%",
      render: (_, record) => <span>{/* Add action buttons here */}</span>,
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey="debtAccountId"
      pagination={tableParams.pagination}
      dataSource={partyDebtAccountPage?.content}
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

export default DebtAccountTable;
