import { useQuery } from "@tanstack/react-query";
import { Space, Table, TablePaginationConfig, Tag } from "antd";
import { TableProps } from "antd/lib";
import { CaretDownFilled, CaretUpFilled } from "@ant-design/icons";
import { useState } from "react";
import { useSearchParams } from "react-router";
import ViewUser from "./ViewUser";
import Access from "../Access";
import UpdateUser from "./UpdateUser";
import { GENDER_NAME, PERMISSIONS } from "../../../common/constants";
import { Gender, Module } from "../../../common/enums";
import { IUser, SortParams } from "../../../interfaces";
import { userService } from "../../../services";
import { getDefaultSortOrder, getSortDirection } from "../../../utils/filter";
import { formatDate } from "../../../utils/datetime";
import { getSortDownIconColor, getSortUpIconColor } from "../../../utils/color";
import UpdateUserStatus from "./UpdateUserStatus";

interface TableParams {
  pagination: TablePaginationConfig;
}

const UserTable: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} người dùng`,
    },
  }));

  const pagination = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const { data, isLoading } = useQuery({
    queryKey: ["users", pagination, sort].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => userService.getPage(pagination, sort),
  });

  const handleTableChange: TableProps<IUser>["onChange"] = (
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

  const columns: TableProps<IUser>["columns"] = [
    {
      title: "Họ và tên",
      key: "fullName",
      dataIndex: "fullName",
      width: "18%",
    },
    {
      title: "Giới tính",
      dataIndex: "gender",
      key: "gender",
      width: "8%",
      render: (gender: Gender) => GENDER_NAME[gender],
    },
    {
      title: "Ngày sinh",
      dataIndex: "dob",
      key: "dob",
      width: "10%",
      render: (dob: string) => dob && formatDate(dob),
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
      title: "Email",
      dataIndex: "email",
      key: "email",
      width: "18%",
    },
    {
      key: "active",
      title: "Trạng thái",
      dataIndex: "active",
      width: "12%",
      render: (active: boolean) => (
        <Tag color={active ? "green" : "red"}>
          {active ? "Đã kích hoạt" : "Chưa kích hoạt"}
        </Tag>
      ),
    },
    {
      title: "Số điện thoại",
      dataIndex: "phoneNumber",
      key: "phoneNumber",
      width: "10%",
    },
    {
      title: "Vai trò",
      dataIndex: "role",
      key: "role",
      width: "10%",
      render: (role) => role.roleName,
    },
    {
      title: "Hành động",
      key: "action",
      render: (record: IUser) => (
        <Space>
          <ViewUser user={record} />
          <Access permission={PERMISSIONS[Module.USER].UPDATE} hideChildren>
            <UpdateUser user={record} />
          </Access>
          <Access
            permission={PERMISSIONS[Module.USER].UPDATE_STATUS}
            hideChildren
          >
            <UpdateUserStatus user={record} />
          </Access>
        </Space>
      ),
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: IUser) => record.userId}
      pagination={tableParams.pagination}
      dataSource={data?.payload?.content}
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

export default UserTable;
