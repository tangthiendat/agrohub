import { FilterFilled } from "@ant-design/icons";
import { FormInstance, Table, Tag } from "antd";
import { TableProps } from "antd/lib";
import { HttpMethod, Module } from "../../../common/enums";
import { IPermission, IRole } from "../../../interfaces";
import { getFilterIconColor, getHttpMethodColor } from "../../../utils/color";
import { useEffect, useState } from "react";

interface Props {
  form: FormInstance<IRole>;
  roleToUpdate?: IRole;
  permissions: IPermission[];
  viewOnly: boolean;
}

const RolePermissions: React.FC<Props> = ({
  form,
  roleToUpdate,
  permissions,
  viewOnly,
}) => {
  const [currentSelectedRowKeys, setCurrentSelectedRowKeys] = useState<
    React.Key[]
  >([]);

  useEffect(() => {
    if (roleToUpdate) {
      setCurrentSelectedRowKeys(
        roleToUpdate.permissions.map((permission) => permission.permissionId),
      );
    }
  }, [roleToUpdate]);

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
      width: "25%",
    },
    {
      title: "Phương thức",
      dataIndex: "httpMethod",
      key: "httpMethod",
      width: "12%",
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
      onFilter: (value, record) => record.httpMethod === value,
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
    {
      title: "Module",
      dataIndex: "module",
      key: "module",
      width: "12%",
      filters: Object.values(Module).map((module: string) => ({
        text: module,
        value: module,
      })),
      onFilter: (value, record) => record.module === value,
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
    {
      title: "Mô tả",
      dataIndex: "description",
      key: "description",
      width: "26%",
    },
  ];

  const rowSelection: TableProps<IPermission>["rowSelection"] = {
    selectedRowKeys: currentSelectedRowKeys,
    onChange: (selectedRowKeys: React.Key[]) => {
      setCurrentSelectedRowKeys(selectedRowKeys);
      form.setFieldsValue({
        permissions: selectedRowKeys.map((key: React.Key) => ({
          permissionId: key as number,
        })),
      });
    },
  };

  function selectRow(record: IPermission): void {
    const selectedRowKeys = [...currentSelectedRowKeys];
    const key = record.permissionId;
    if (selectedRowKeys.includes(key)) {
      selectedRowKeys.splice(selectedRowKeys.indexOf(key), 1);
    } else {
      selectedRowKeys.push(key);
    }
    setCurrentSelectedRowKeys(selectedRowKeys);
    form.setFieldsValue({
      permissions: selectedRowKeys.map((key: React.Key) => ({
        permissionId: key as number,
      })),
    });
  }

  return (
    <Table
      bordered={false}
      rowKey={(record: IPermission) => record.permissionId}
      dataSource={permissions}
      columns={columns}
      pagination={{
        showSizeChanger: true,
        showTotal: (total) => `Tổng ${total} quyền hạn`,
      }}
      rowSelection={viewOnly ? undefined : rowSelection}
      onRow={(record: IPermission) => ({
        onClick: () => !viewOnly && selectRow(record),
      })}
      size="small"
      rowClassName={(_, index) =>
        index % 2 === 0 ? "table-row-light" : "table-row-gray"
      }
    />
  );
};

export default RolePermissions;
