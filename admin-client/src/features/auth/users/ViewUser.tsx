import { useState } from "react";
import { IUser } from "../../../interfaces";
import { Descriptions, DescriptionsProps, Drawer, Tag } from "antd";
import ViewDetailsIcon from "../../../common/components/icons/ViewDetailsIcon";
import { GENDER_NAME } from "../../../common/constants";
import { formatTimestamp } from "../../../utils/datetime";

interface Props {
  user: IUser;
}

const ViewUser: React.FC<Props> = ({ user }) => {
  const [open, setOpen] = useState<boolean>(false);

  function showDrawer(): void {
    setOpen(true);
  }

  function onClose(): void {
    setOpen(false);
  }

  const items: DescriptionsProps["items"] = [
    {
      key: "userId",
      label: "ID",
      span: "filled",
      children: user.userId,
    },
    {
      key: "fullName",
      label: "Họ và tên",
      span: "filled",
      children: user.fullName,
    },
    {
      key: "gender",
      label: "Giới tính",
      span: "filled",
      children: GENDER_NAME[user.gender],
    },
    {
      key: "email",
      label: "Email",
      span: "filled",
      children: user.email,
    },
    {
      key: "phoneNumber",
      label: "Số điện thoại",
      span: "filled",
      children: user.phoneNumber,
    },
    {
      key: "active",
      label: "Trạng thái",
      span: "filled",
      children: (
        <Tag color={user.active ? "green" : "red"}>
          {user.active ? "Đã kích hoạt" : "Chưa kích hoạt"}
        </Tag>
      ),
    },
    {
      key: "role",
      label: "Vai trò",
      span: "filled",
      children: user.role.roleName,
    },
    {
      key: "createdAt",
      label: "Ngày tạo",
      span: "filled",
      children: user.createdAt && formatTimestamp(user.createdAt),
    },
    {
      key: "updatedAt",
      label: "Ngày cập nhật",
      span: "filled",
      children: user.updatedAt && formatTimestamp(user.updatedAt),
    },
  ];
  return (
    <>
      <ViewDetailsIcon onClick={showDrawer} tooltipTitle="Xem chi tiết" />
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

export default ViewUser;
