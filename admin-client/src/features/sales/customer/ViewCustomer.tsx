import { Descriptions, DescriptionsProps, Drawer, Tag } from "antd";
import { useState } from "react";
import { ICustomer } from "../../../interfaces";
import { formatTimestamp } from "../../../utils/datetime";
import ViewIcon from "../../../common/components/icons/ViewIcon";
import { CUSTOMER_TYPE_NAME } from "../../../common/constants";

interface ViewCustomerProps {
  customer: ICustomer;
}

const ViewCustomer: React.FC<ViewCustomerProps> = ({ customer }) => {
  const [open, setOpen] = useState<boolean>(false);

  function showDrawer(): void {
    setOpen(true);
  }

  function onClose(): void {
    setOpen(false);
  }

  const items: DescriptionsProps["items"] = [
    {
      label: "ID",
      key: "customerId",
      span: "filled",
      children: customer.customerId,
    },
    {
      label: "Tên khách hàng",
      key: "customerName",
      span: "filled",
      children: customer.customerName,
    },
    {
      label: "Trạng thái",
      key: "active",
      span: "filled",
      children: (
        <Tag color={customer.active ? "green" : "red"}>
          {customer.active ? "Đã kích hoạt" : "Chưa kích hoạt"}
        </Tag>
      ),
    },
    {
      label: "Loại khách hàng",
      key: "customerType",
      span: "filled",
      children: CUSTOMER_TYPE_NAME[customer.customerType],
    },
    {
      label: "Email",
      key: "email",
      span: "filled",
      children: customer.email,
    },

    {
      label: "Số điện thoại",
      key: "phoneNumber",
      span: "filled",
      children: customer.phoneNumber,
    },
    {
      label: "Địa chỉ",
      key: "address",
      span: "filled",
      children: customer.address,
    },
    {
      label: "Ghi chú",
      key: "notes",
      span: "filled",
      children: customer.notes,
    },
    {
      key: "createdAt",
      label: "Ngày tạo",
      span: "filled",
      children: customer.createdAt && formatTimestamp(customer.createdAt),
    },
    {
      key: "updatedAt",
      label: "Ngày cập nhật",
      span: "filled",
      children: customer.updatedAt && formatTimestamp(customer.updatedAt),
    },
  ];

  return (
    <>
      <ViewIcon onClick={showDrawer} tooltipTitle="Xem chi tiết" />
      <Drawer
        width="40%"
        open={open}
        onClose={onClose}
        title="Chi tiết khách hàng"
      >
        <Descriptions
          size="middle"
          bordered
          items={items}
          labelStyle={{
            width: "28%",
          }}
        />
      </Drawer>
    </>
  );
};

export default ViewCustomer;
