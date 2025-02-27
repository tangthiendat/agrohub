import { useQuery } from "@tanstack/react-query";
import { Descriptions, DescriptionsProps, Drawer, Tag } from "antd";
import { useState } from "react";
import ViewIcon from "../../../common/components/icons/ViewIcon";
import Loading from "../../../common/components/Loading";
import WarehouseOption from "../../warehouse/WarehouseOption";
import { GENDER_NAME } from "../../../common/constants";
import { IUser } from "../../../interfaces";
import { warehouseService } from "../../../services";
import { formatTimestamp } from "../../../utils/datetime";

interface ViewUserProps {
  user: IUser;
}

const ViewUser: React.FC<ViewUserProps> = ({ user }) => {
  const [open, setOpen] = useState<boolean>(false);

  const { data: warehouse, isLoading } = useQuery({
    queryKey: ["warehouse", user.warehouseId],
    queryFn: () => warehouseService.getById(user.warehouseId),
    enabled: open,
    select: (data) => data.payload,
  });

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
      label: "Kho làm việc",
      key: "warehouse",
      span: "filled",
      children: warehouse ? <WarehouseOption warehouse={warehouse} /> : "",
    },
    {
      label: "Vai trò",
      key: "role",
      span: "filled",
      children: user.role.roleName,
    },
    {
      label: "Ngày tạo",
      key: "createdAt",
      span: "filled",
      children: user.createdAt && formatTimestamp(user.createdAt),
    },
    {
      label: "Ngày cập nhật",
      key: "updatedAt",
      span: "filled",
      children: user.updatedAt && formatTimestamp(user.updatedAt),
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
        {isLoading ? (
          <Loading />
        ) : (
          <Descriptions
            size="middle"
            bordered
            items={items}
            labelStyle={{
              width: "27%",
            }}
          />
        )}
      </Drawer>
    </>
  );
};

export default ViewUser;
