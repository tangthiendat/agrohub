import { useState } from "react";
import { Descriptions, DescriptionsProps, Drawer, Tag, Typography } from "antd";
import ViewIcon from "../../common/components/icons/ViewIcon";
import { ISupplier } from "../../interfaces";
import { formatTimestamp } from "../../utils/datetime";

interface ViewSupplierProps {
  supplier: ISupplier;
}

const ViewSupplier: React.FC<ViewSupplierProps> = ({ supplier }) => {
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
      key: "supplierId",
      span: "filled",
      children: supplier.supplierId,
    },
    {
      label: "Tên nhà cung cấp",
      key: "supplierName",
      span: "filled",
      children: supplier.supplierName,
    },
    {
      label: "Trạng thái",
      key: "active",
      span: "filled",
      children: (
        <Tag color={supplier.active ? "green" : "red"}>
          {supplier.active ? "Đã kích hoạt" : "Chưa kích hoạt"}
        </Tag>
      ),
    },
    {
      label: "Email",
      key: "email",
      span: "filled",
      children: supplier.email,
    },

    {
      label: "Số điện thoại",
      key: "phoneNumber",
      span: "filled",
      children: supplier.phoneNumber,
    },
    {
      label: "Địa chỉ",
      key: "address",
      span: "filled",
      children: supplier.address,
    },
    {
      key: "contactPerson",
      label: "Người liên hệ",
      span: "filled",
      children: supplier.contactPerson,
    },
    {
      label: "Ghi chú",
      key: "notes",
      span: "filled",
      children: supplier.notes,
    },
    {
      key: "createdAt",
      label: "Ngày tạo",
      span: "filled",
      children: supplier.createdAt && formatTimestamp(supplier.createdAt),
    },
    {
      key: "updatedAt",
      label: "Ngày cập nhật",
      span: "filled",
      children: supplier.updatedAt && formatTimestamp(supplier.updatedAt),
    },
  ];

  return (
    <>
      <ViewIcon onClick={showDrawer} tooltipTitle="Xem chi tiết" />
      <Drawer
        width="40%"
        open={open}
        onClose={onClose}
        title="Chi tiết nhà cung cấp"
      >
        <Typography.Title level={5} className="mb-2">
          Thông tin cơ bản
        </Typography.Title>
        <Descriptions
          className="mb-4"
          size="middle"
          bordered
          items={items}
          labelStyle={{
            width: "28%",
          }}
        />
        <Typography.Title level={5} className="mb-2">
          Thông tin đánh giá
        </Typography.Title>
        <Descriptions
          size="middle"
          bordered
          items={[
            {
              key: "trustScore",
              label: "Điểm tín nhiệm",
              span: "filled",
              children:
                supplier.supplierRating &&
                `${supplier.supplierRating.trustScore}/100`,
            },
            {
              key: "comment",
              label: "Nhận xét",
              span: "filled",
              children: supplier.supplierRating?.comment,
            },
            {
              key: "updatedAt",
              label: "Cập nhật",
              span: "filled",
              children:
                supplier.supplierRating?.updatedAt &&
                formatTimestamp(supplier.supplierRating.updatedAt),
            },
          ]}
          labelStyle={{
            width: "28%",
          }}
        />
      </Drawer>
    </>
  );
};

export default ViewSupplier;
