import {
  Button,
  Descriptions,
  DescriptionsProps,
  Form,
  InputNumber,
  Modal,
  Space,
  Table,
  TableProps,
  Tag,
  Typography,
} from "antd";
import {
  IProductBatch,
  IProductBatchLocation,
  IProductLocation,
} from "../../../interfaces";
import { formatDate } from "../../../utils/datetime";
import SearchProductLocationBar from "../location/SearchProductLocationBar";
import { getLocationName } from "../../../utils/data";
import {
  LOCATION_STATUS_COLOR,
  LOCATION_STATUS_NAME,
  RACK_TYPE_NAME,
} from "../../../common/constants";
import { useState } from "react";
import DeleteIcon from "../../../common/components/icons/DeleteIcon";

interface UpdateBatchLocationFormProps {
  productBatch: IProductBatch;
  onCancel: () => void;
}

const UpdateBatchLocationForm: React.FC<UpdateBatchLocationFormProps> = ({
  productBatch,
  onCancel,
}) => {
  const [form] = Form.useForm<IProductBatch>();
  const [currentProductBatch, setCurrentProductBatch] = useState<IProductBatch>(
    { ...productBatch, batchLocations: productBatch.batchLocations || [] },
  );
  const [modal, contextHolder] = Modal.useModal();

  function onSelectLocation(productLocation: IProductLocation) {
    if (
      currentProductBatch.batchLocations.some(
        (item) => item.location.locationId === productLocation.locationId,
      )
    ) {
      modal.error({
        title: "Vị trí đã tồn tại",
        content: "Vui lòng chọn vị trí khác",
      });
      return;
    }

    //check sum of quantity of all locations is equal to quantity of batch
    const sumQuantity = currentProductBatch.batchLocations.reduce(
      (sum, item) => sum + item.quantity,
      0,
    );
    if (sumQuantity >= currentProductBatch.quantity) {
      modal.error({
        title: "Số lượng lô hàng đã đủ",
        content:
          "Vui lòng điều chỉnh số lượng tại các vị trí khác trước khi thêm mới",
      });
      return;
    }

    setCurrentProductBatch((prev) => ({
      ...prev,
      batchLocations: [
        ...prev.batchLocations,
        { location: productLocation, quantity: 0 },
      ],
    }));
    form.setFieldsValue({
      batchLocations: [
        ...currentProductBatch.batchLocations,
        { location: productLocation, quantity: 0 },
      ],
    });
  }

  function handleFinish() {
    //check if locations is empty
    if (currentProductBatch.batchLocations.length === 0) {
      modal.error({
        title: "Chưa chọn vị trí",
        content: "Vui lòng chọn ít nhất 1 vị trí",
      });
      return;
    }
    //check if sum of quantity of all locations is equal to quantity of batch
    const sumQuantity = currentProductBatch.batchLocations.reduce(
      (sum, item) => sum + item.quantity,
      0,
    );
    if (sumQuantity !== currentProductBatch.quantity) {
      modal.error({
        title: "Số lượng không đúng",
        content: "Vui lòng kiểm tra lại số lượng từng vị trí",
      });
      return;
    }
    console.log(currentProductBatch);
  }

  const items: DescriptionsProps["items"] = [
    {
      label: "Mã lô hàng",
      key: "batchId",
      span: {
        xl: 1,
        lg: 3,
        md: 3,
        sm: 3,
      },
      children: <span className="font-semibold">{productBatch.batchId}</span>,
    },
    {
      label: "Tên sản phẩm",
      key: "productName",
      span: "filled",
      children: productBatch.product.productName,
    },
    {
      label: "Ngày sản xuất",
      key: "manufacturingDate",
      span: {
        xl: 1,
        lg: 3,
        md: 3,
        sm: 3,
      },
      children: formatDate(productBatch.manufacturingDate),
    },
    {
      label: "Hạn sử dụng",
      key: "expirationDate",
      span: {
        xl: 1,
        lg: 3,
        md: 3,
        sm: 3,
      },
      children: formatDate(productBatch.expirationDate),
    },
    {
      label: "Ngày nhập kho",
      key: "receivedDate",
      span: {
        xl: 1,
        lg: 3,
        md: 3,
        sm: 3,
      },
      children: formatDate(productBatch.receivedDate),
    },
    {
      label: "Số lượng",
      key: "quantity",
      children: productBatch.quantity,
      span: {
        xl: 1,
        lg: 3,
        md: 3,
        sm: 3,
      },
    },
  ];
  const columns: TableProps<IProductBatchLocation>["columns"] = [
    {
      title: "Vị trí",
      dataIndex: "location",
      key: "location",
      width: "20%",
      render: (location: IProductLocation) => getLocationName(location),
    },
    {
      title: "Loại kệ",
      key: "rackType",
      width: "25%",
      render: (_, record: IProductBatchLocation) =>
        RACK_TYPE_NAME[record.location.rackType],
    },
    {
      title: "Số lượng",
      dataIndex: "quantity",
      key: "quantity",
      width: "25%",
      render: (quantity: number, record: IProductBatchLocation) => (
        <InputNumber
          value={quantity}
          min={0}
          max={currentProductBatch.quantity}
          status={
            currentProductBatch.batchLocations.reduce(
              (sum, item) => sum + item.quantity,
              0,
            ) > currentProductBatch.quantity
              ? "error"
              : undefined
          }
          onChange={(value) => {
            if (value) {
              setCurrentProductBatch((prev) => ({
                ...prev,
                batchLocations: prev.batchLocations.map((item) =>
                  item.location.locationId === record.location.locationId
                    ? { ...item, quantity: value }
                    : item,
                ),
              }));
              form.setFieldsValue({
                batchLocations: currentProductBatch.batchLocations.map(
                  (item) =>
                    item.location.locationId === record.location.locationId
                      ? { ...item, quantity: value }
                      : item,
                ),
              });
            }
          }}
        />
      ),
    },
    {
      title: "Hành động",
      key: "action",
      width: "30%",
      render: (_, record: IProductBatchLocation) => {
        return (
          <DeleteIcon
            tooltipTitle="Xóa"
            onClick={() => {
              setCurrentProductBatch((prev) => ({
                ...prev,
                batchLocations: prev.batchLocations.filter(
                  (item) =>
                    item.location.locationId !== record.location.locationId,
                ),
              }));
              form.setFieldsValue({
                batchLocations: currentProductBatch.batchLocations.filter(
                  (item) =>
                    item.location.locationId !== record.location.locationId,
                ),
              });
            }}
          />
        );
      },
    },
  ];
  return (
    <>
      {contextHolder}
      <Form form={form} onFinish={handleFinish}>
        <Descriptions title="Thông tin lô hàng" items={items} />
        <Typography.Title level={5} className="my-3">
          Vị trí lô hàng
        </Typography.Title>
        <SearchProductLocationBar
          className="w-[40%]"
          placeholder="Tìm kiếm vị trí"
          onSelect={onSelectLocation}
          optionRenderer={(location) => {
            return {
              value: location.locationId,
              label: (
                <div className="flex flex-col">
                  <div className="flex items-center justify-between">
                    <div className="font-semibold">
                      {getLocationName(location)}
                    </div>
                    <Tag color={LOCATION_STATUS_COLOR[location.status]}>
                      {LOCATION_STATUS_NAME[location.status]}
                    </Tag>
                  </div>
                  <div className="text-sm">
                    <span className="text-gray-600">
                      {RACK_TYPE_NAME[location.rackType]}
                    </span>
                  </div>
                </div>
              ),
            };
          }}
        />
        <Table
          className="mt-3"
          size="small"
          columns={columns}
          dataSource={currentProductBatch.batchLocations}
          rowKey={(record) => record.location.locationId}
          pagination={false}
        />
        <Form.Item className="mt-4 text-right" wrapperCol={{ span: 24 }}>
          <Space>
            <Button onClick={onCancel}>Hủy</Button>
            <Button type="primary" htmlType="submit">
              Cập nhật
            </Button>
          </Space>
        </Form.Item>
      </Form>
    </>
  );
};

export default UpdateBatchLocationForm;
