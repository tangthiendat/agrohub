import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  Alert,
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
import { useState } from "react";
import toast from "react-hot-toast";
import SearchProductLocationBar from "../location/SearchProductLocationBar";
import DeleteIcon from "../../../common/components/icons/DeleteIcon";
import {
  LOCATION_STATUS_COLOR,
  LOCATION_STATUS_NAME,
  RACK_TYPE_NAME,
} from "../../../common/constants";
import { LocationStatus } from "../../../common/enums";
import {
  IProductBatch,
  IProductBatchLocation,
  IProductLocation,
} from "../../../interfaces";
import { productBatchService } from "../../../services";
import { getLocationName } from "../../../utils/data";
import { formatDate } from "../../../utils/datetime";
import { getNotificationMessage } from "../../../utils/notification";

interface UpdateBatchLocationFormProps {
  productBatch: IProductBatch;
  onCancel: () => void;
  viewOnly?: boolean;
}

interface UpdateProductBatchArgs {
  batchId: string;
  updatedProductBatch: IProductBatch;
}

const UpdateBatchLocationForm: React.FC<UpdateBatchLocationFormProps> = ({
  productBatch,
  onCancel,
  viewOnly = false,
}) => {
  const [form] = Form.useForm<IProductBatch>();
  const [currentProductBatch, setCurrentProductBatch] = useState<IProductBatch>(
    { ...productBatch, batchLocations: productBatch.batchLocations || [] },
  );
  const queryClient = useQueryClient();
  const [modal, contextHolder] = Modal.useModal();

  const { mutate: updateProductBatch, isPending: isUpdating } = useMutation({
    mutationFn: ({ batchId, updatedProductBatch }: UpdateProductBatchArgs) =>
      productBatchService.update(batchId, updatedProductBatch),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["product-batches"],
      });
    },
  });

  function onSelectLocation(productLocation: IProductLocation) {
    //check if location status is not AVAILABLE
    if (productLocation.status !== LocationStatus.AVAILABLE) {
      modal.error({
        title: "Vị trí không khả dụng",
        content: "Vui lòng chọn vị trí khác",
      });
      return;
    }

    //check if location is already in currentProductBatch
    if (
      currentProductBatch.batchLocations.some(
        (item) =>
          item.productLocation.locationId === productLocation.locationId,
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
        { productLocation, quantity: 0 },
      ],
    }));
    form.setFieldsValue({
      batchLocations: [
        ...currentProductBatch.batchLocations,
        { productLocation, quantity: 0 },
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
    updateProductBatch(
      {
        batchId: currentProductBatch.batchId,
        updatedProductBatch: currentProductBatch,
      },
      {
        onSuccess: () => {
          toast.success("Cập nhật vị trí lô hàng thành công");
          form.resetFields();
          onCancel();
        },
        onError: (error: Error) => {
          toast.error(
            getNotificationMessage(error) || "Cập nhật vị trí lô hàng thất bại",
          );
        },
      },
    );
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
      dataIndex: "productLocation",
      key: "location",
      width: "20%",
      render: (location: IProductLocation) =>
        location ? getLocationName(location) : "",
    },
    {
      title: "Loại kệ",
      key: "rackType",
      width: "25%",
      render: (_, record: IProductBatchLocation) =>
        RACK_TYPE_NAME[record.productLocation.rackType],
    },
    {
      title: "Số lượng",
      dataIndex: "quantity",
      key: "quantity",
      width: "25%",
      align: "right",
      render: (quantity: number, record: IProductBatchLocation) => (
        <InputNumber
          value={quantity}
          controls={false}
          readOnly={viewOnly}
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
                  item.productLocation.locationId ===
                  record.productLocation.locationId
                    ? { ...item, quantity: value }
                    : item,
                ),
              }));
              form.setFieldsValue({
                batchLocations: currentProductBatch.batchLocations.map(
                  (item) =>
                    item.productLocation.locationId ===
                    record.productLocation.locationId
                      ? { ...item, quantity: value }
                      : item,
                ),
              });
            }
          }}
        />
      ),
    },
    ...(viewOnly
      ? []
      : [
          {
            title: "Hành động",
            key: "action",
            width: "30%",
            render: (_: unknown, record: IProductBatchLocation) => {
              return (
                !record.batchLocationId && (
                  <DeleteIcon
                    tooltipTitle="Xóa"
                    onClick={() => {
                      setCurrentProductBatch((prev) => ({
                        ...prev,
                        batchLocations: prev.batchLocations.filter(
                          (item) =>
                            item.productLocation.locationId !==
                            record.productLocation.locationId,
                        ),
                      }));
                      form.setFieldsValue({
                        batchLocations:
                          currentProductBatch.batchLocations.filter(
                            (item) =>
                              item.productLocation.locationId !==
                              record.productLocation.locationId,
                          ),
                      });
                    }}
                  />
                )
              );
            },
          },
        ]),
  ];

  return (
    <>
      {contextHolder}
      <Form form={form} onFinish={handleFinish}>
        <Descriptions title="Thông tin lô hàng" items={items} />
        <Typography.Title level={5} className="my-3">
          Vị trí lô hàng
        </Typography.Title>
        {!viewOnly && (
          <Alert
            // message="Lưu ý"
            description="Đối với các sản phẩm có kích thước lớn (vd, bao, ...) thì hãy lựa chọn kệ pallet"
            type="warning"
            showIcon
            className="mb-4"
          />
        )}
        {!viewOnly && (
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
        )}
        <Table
          className="mt-3"
          size="small"
          columns={columns}
          dataSource={currentProductBatch.batchLocations}
          rowKey={(record) => record.productLocation.locationId}
          pagination={false}
        />
        {!viewOnly && (
          <Form.Item className="mt-4 text-right" wrapperCol={{ span: 24 }}>
            <Space>
              <Button onClick={onCancel} loading={isUpdating}>
                Hủy
              </Button>
              <Button type="primary" htmlType="submit" loading={isUpdating}>
                Cập nhật
              </Button>
            </Space>
          </Form.Item>
        )}
      </Form>
    </>
  );
};

export default UpdateBatchLocationForm;
