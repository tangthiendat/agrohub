import {
  CaretDownFilled,
  CaretUpFilled,
  FilterFilled,
} from "@ant-design/icons";
import { useQuery } from "@tanstack/react-query";
import { Space, Table, TablePaginationConfig, Tag } from "antd";
import { TableProps } from "antd/lib";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import { GENDER_NAME, PERMISSIONS } from "../../../common/constants";
import { Gender, Module } from "../../../common/enums";
import { IUser, Page } from "../../../interfaces";
import { roleService } from "../../../services/auth/role-service";
import {
  getFilterIconColor,
  getSortDownIconColor,
  getSortUpIconColor,
} from "../../../utils/color";
import { formatDate } from "../../../utils/datetime";
import {
  getDefaultFilterValue,
  getDefaultSortOrder,
  getSortDirection,
} from "../../../utils/filter";
import Access from "../Access";
import UpdateUser from "./UpdateUser";
import UpdateUserStatus from "./UpdateUserStatus";
import ViewUser from "./ViewUser";

interface UserTableProps {
  userPage?: Page<IUser>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const UserTable: React.FC<UserTableProps> = ({ userPage, isLoading }) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} người dùng`,
    },
  }));

  const { data: roleOptions, isLoading: isRolesLoading } = useQuery({
    queryKey: ["roles"],
    queryFn: roleService.getAll,
    select: (data) => {
      if (data.payload) {
        return data.payload.map((role) => ({
          value: role.roleId,
          text: role.roleName,
        }));
      }
    },
  });

  useEffect(() => {
    if (userPage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: userPage.meta?.totalElements || 0,
        },
      }));
    }
  }, [userPage]);

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

    if (filters) {
      Object.entries(filters).forEach(([key, value]) => {
        const paramKey = key === "role" ? "roleId" : key;
        if (!value) {
          searchParams.delete(paramKey);
          return;
        }
        const paramValue = Array.isArray(value)
          ? value.join(",")
          : String(value);

        searchParams.set(paramKey, paramValue);
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
      filters: Object.entries(GENDER_NAME).map(([key, value]) => ({
        text: value,
        value: key,
      })),
      defaultFilteredValue: getDefaultFilterValue(searchParams, "gender"),
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
    {
      title: "Ngày sinh",
      dataIndex: "dob",
      key: "dob",
      width: "10%",
      render: (dob: string) => dob && formatDate(dob),
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "dob"),
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
      filters: roleOptions,
      defaultFilteredValue: getDefaultFilterValue(searchParams, "roleId")?.map(
        (roleId) => Number(roleId),
      ),
      filterIcon: () => {
        const isFiltered = Boolean(
          getDefaultFilterValue(searchParams, "roleId")?.length,
        );
        return (
          <FilterFilled style={{ color: getFilterIconColor(isFiltered) }} />
        );
      },
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
      dataSource={userPage?.content}
      rowClassName={(_, index) =>
        index % 2 === 0 ? "table-row-light" : "table-row-gray"
      }
      rowHoverable={false}
      loading={{
        spinning: isLoading || isRolesLoading,
        tip: "Đang tải dữ liệu...",
      }}
      onChange={handleTableChange}
      size="middle"
    />
  );
};

export default UserTable;
