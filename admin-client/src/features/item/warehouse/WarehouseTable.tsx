import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import { useQuery } from "@tanstack/react-query";
import { Space, Table, TablePaginationConfig } from "antd";
import { TableProps } from "antd/lib";
import { CaretDownFilled, CaretUpFilled } from "@ant-design/icons";
import Access from "../../auth/Access";
import UpdateWarehouse from "./UpdateWarehouse";
import { IWarehouse, PaginationParams, SortParams } from "../../../interfaces";
import { warehouseService } from "../../../services";
import { getDefaultSortOrder, getSortDirection } from "../../../utils/filter";
import { formatTimestamp } from "../../../utils/datetime";
import { getSortDownIconColor, getSortUpIconColor } from "../../../utils/color";
import { PERMISSIONS } from "../../../common/constants";
import { Module } from "../../../common/enums";

interface TableParams {
  pagination: TablePaginationConfig;
}

const WarehouseTable: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} loại kho hàng`,
    },
  }));

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const { data, isLoading } = useQuery({
    queryKey: ["warehouses", pagination, sort].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => warehouseService.getPage(pagination, sort),
  });

  useEffect(() => {
    if (data) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: data.payload?.meta.totalElements,
        },
      }));
    }
  }, [data]);

  const handleTableChange: TableProps<IWarehouse>["onChange"] = (
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

  const columns: TableProps<IWarehouse>["columns"] = [
    {
      title: "ID",
      dataIndex: "warehouseId",
      key: "warehouseId",
      width: "5%",
    },
    {
      title: "Tên kho hàng",
      dataIndex: "warehouseName",
      key: "warehouseName",
      width: "10%",
    },
    {
      title: "Địa chỉ kho hàng",
      dataIndex: "address",
      key: "address",
      width: "25%",
    },

    {
      title: "Ngày tạo",
      dataIndex: "createdAt",
      key: "createdAt",
      width: "15%",
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
      title: "Ngày cập nhật",
      dataIndex: "updatedAt",
      key: "updatedAt",
      width: "15%",
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
      width: "15%",
      render: (record: IWarehouse) => (
        <Space>
          <Access
            permission={PERMISSIONS[Module.WAREHOUSE].UPDATE}
            hideChildren
          >
            <UpdateWarehouse warehouse={record} />
          </Access>
        </Space>
      ),
    },
  ];

  return (
    <Table
      rowKey={(warehouse: IWarehouse) => warehouse.warehouseId}
      dataSource={data?.payload?.content}
      columns={columns}
      pagination={tableParams.pagination}
      bordered={false}
      size="middle"
      rowClassName={(_, index) =>
        index % 2 === 0 ? "table-row-light" : "table-row-gray"
      }
      rowHoverable={false}
      loading={{
        spinning: isLoading,
        tip: "Đang tải dữ liệu...",
      }}
      onChange={handleTableChange}
    />
  );
};

export default WarehouseTable;
