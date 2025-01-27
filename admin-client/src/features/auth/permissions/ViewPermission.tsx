import { Descriptions, DescriptionsProps, Drawer, Tag } from "antd";
import { useState } from "react";
import ViewIcon from "../../../common/components/icons/ViewIcon";
import { IPermission } from "../../../interfaces";
import { getHttpMethodColor } from "../../../utils/color";
import { formatTimestamp } from "../../../utils/datetime";

interface ViewPermissionProps {
  permission: IPermission;
}

const ViewPermission: React.FC<ViewPermissionProps> = ({ permission }) => {
  const [open, setOpen] = useState<boolean>(false);

  function showDrawer(): void {
    setOpen(true);
  }

  function onClose(): void {
    setOpen(false);
  }

  const items: DescriptionsProps["items"] = [
    {
      key: "id",
      label: "ID",
      span: "filled",
      children: permission.permissionId,
    },
    {
      key: "permissionName",
      label: "Tên quyền hạn",
      span: "filled",
      children: permission.permissionName,
    },
    {
      key: "apiPath",
      label: "Đường dẫn API",
      span: "filled",
      children: permission.apiPath,
    },
    {
      key: "httpMethod",
      label: "Phương thức",
      span: "filled",
      children: (
        <Tag
          className="font-bold"
          color={getHttpMethodColor(permission.httpMethod)}
        >
          {permission.httpMethod}
        </Tag>
      ),
    },
    {
      key: "module",
      label: "Module",
      span: "filled",
      children: permission.module,
    },
    {
      key: "description",
      label: "Mô tả",
      span: "filled",
      children: permission.description,
    },
    {
      key: "createdAt",
      label: "Ngày tạo",
      span: "filled",
      children: permission.createdAt && formatTimestamp(permission.createdAt),
    },
    {
      key: "updatedAt",
      label: "Ngày cập nhật",
      span: "filled",
      children: permission.updatedAt && formatTimestamp(permission.updatedAt),
    },
  ];
  return (
    <>
      <ViewIcon onClick={showDrawer} tooltipTitle="Xem chi tiết" />
      <Drawer
        width="40%"
        open={open}
        onClose={onClose}
        title="Chi tiết quyền hạn"
      >
        <Descriptions
          size="middle"
          bordered
          items={items}
          labelStyle={{
            width: "27%",
          }}
        />
      </Drawer>
    </>
  );
};

export default ViewPermission;
