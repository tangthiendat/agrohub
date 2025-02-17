import {
  CaretDownFilled,
  CaretUpFilled,
  FilterFilled,
} from "@ant-design/icons";
import { useQuery } from "@tanstack/react-query";
import { Space, Table, TablePaginationConfig, TableProps, Tag } from "antd";
import { SorterResult } from "antd/es/table/interface";
import { GetProp } from "antd/lib";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import Access from "../Access";
import UpdatePermission from "./UpdatePermission";
import ViewPermission from "./ViewPermission";
import { PERMISSIONS } from "../../../common/constants";
import { HttpMethod, Module } from "../../../common/enums";
import {
  IPermission,
  PaginationParams,
  PermissionFilterCriteria,
  SortParams,
} from "../../../interfaces";
import { permissionService } from "../../../services";
import {
  getFilterIconColor,
  getHttpMethodColor,
  getSortDownIconColor,
  getSortUpIconColor,
} from "../../../utils/color";
import { formatTimestamp } from "../../../utils/datetime";
import {
  getDefaultFilterValue,
  getDefaultSortOrder,
  getSortDirection,
} from "../../../utils/filter";

interface TableParams {
  pagination: TablePaginationConfig;
  sorter?: SorterResult<IPermission> | SorterResult<IPermission>[];
  filters?: Parameters<GetProp<TableProps, "onChange">>[1];
}

const PermissionTable: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} quyền hạn`,
    },
  }));

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };
  const filter: PermissionFilterCriteria = {
    httpMethod: searchParams.get("httpMethod") || undefined,
    module: searchParams.get("module") || undefined,
  };
  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const { data, isLoading } = useQuery({
    queryKey: ["permissions", pagination, filter, sort].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => permissionService.getPage(pagination, sort, filter),
  });

  useEffect(() => {
    if (data) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: data?.payload?.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} quyền hạn`,
        },
      }));
    }
  }, [data]);

  const handleTableChange: TableProps<IPermission>["onChange"] = (
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

  const columns: TableProps<IPermission>["columns"] = [
    {
      title: "Tên quyền hạn",
      dataIndex: "permissionName",
      key: "permissionName",
      width: "20%",
    },
    {
      title: "Đường dẫn API",
      dataIndex: "apiPath",
      key: "apiPath",
    },
    {
      title: "Phương thức",
      dataIndex: "httpMethod",
      key: "httpMethod",
      width: "10%",
      render(httpMethod: IPermission["httpMethod"]) {
        return (
          <Tag className="font-bold" color={getHttpMethodColor(httpMethod)}>
            {httpMethod}
          </Tag>
        );
      },
      filters: Object.values(HttpMethod).map((httpMethod: string) => ({
        text: httpMethod,
        value: httpMethod,
      })),
      defaultFilteredValue: getDefaultFilterValue(searchParams, "httpMethod"),
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
    {
      title: "Module",
      dataIndex: "module",
      key: "module",
      width: "10%",
      filters: Object.values(Module).map((module: string) => ({
        text: module,
        value: module,
      })),
      defaultFilteredValue: getDefaultFilterValue(searchParams, "module"),
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
    {
      title: "Thời gian tạo",
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
      title: "Thời gian cập nhật",
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
      width: "10%",
      render: (record: IPermission) => (
        <Space>
          <ViewPermission permission={record} />
          <Access
            permission={PERMISSIONS[Module.PERMISSION].UPDATE}
            hideChildren
          >
            <UpdatePermission permission={record} />
          </Access>
          {/* <Access permission={PERMISSIONS[Module.PERMISSION].DELETE}>
            <DeletePermission permissionId={record.permissionId} />
          </Access> */}
        </Space>
      ),
    },
  ];
  return (
    <Table
      bordered={false}
      rowKey={(record: IPermission) => record.permissionId}
      dataSource={data?.payload?.content || []}
      columns={columns}
      pagination={tableParams.pagination}
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

export default PermissionTable;
