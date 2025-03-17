import {
  Button,
  Form,
  Input,
  InputNumber,
  Select,
  Skeleton,
  Space,
} from "antd";
import { useEffect } from "react";
import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { IProductLocation } from "../../../interfaces";
import { useCurrentWarehouse } from "../../../common/hooks";
import { LocationStatus, RackType } from "../../../common/enums";
import {
  LOCATION_STATUS_NAME,
  RACK_TYPE_NAME,
} from "../../../common/constants";
import { productLocationService } from "../../../services";
import { getNotificationMessage } from "../../../utils/notification";

interface UpdateProductLocationFormProps {
  productLocationToUpdate?: IProductLocation;
  onCancel: () => void;
}

interface UpdateProductLocationArgs {
  productLocationId: string;
  updatedProductLocation: IProductLocation;
}

const UpdateProductLocationForm: React.FC<UpdateProductLocationFormProps> = ({
  productLocationToUpdate,
  onCancel,
}) => {
  const [form] = Form.useForm<IProductLocation>();
  const queryClient = useQueryClient();
  const { currentWarehouse, isLoading: isWarehouseLoading } =
    useCurrentWarehouse();

  const { mutate: createProductLocation, isPending: isCreating } = useMutation({
    mutationFn: productLocationService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["product-locations"],
      });
    },
  });

  const { mutate: updateProductLocation, isPending: isUpdating } = useMutation({
    mutationFn: ({
      productLocationId,
      updatedProductLocation,
    }: UpdateProductLocationArgs) =>
      productLocationService.update(productLocationId, updatedProductLocation),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["product-locations"],
      });
    },
  });

  useEffect(() => {
    if (productLocationToUpdate) {
      form.setFieldsValue(productLocationToUpdate);
    }
  }, [form, productLocationToUpdate]);

  function handleFinish(values: IProductLocation) {
    if (productLocationToUpdate) {
      const updatedProductLocation = {
        ...productLocationToUpdate,
        ...values,
      };
      updateProductLocation(
        {
          productLocationId: productLocationToUpdate.locationId,
          updatedProductLocation,
        },
        {
          onSuccess: () => {
            toast.success("Cập nhật vị trí hàng hóa thành công");
            form.resetFields();
            onCancel();
          },
          onError: (error: Error) => {
            toast.error(
              getNotificationMessage(error) ||
                "Cập nhật vị trí hàng hóa thất bại",
            );
          },
        },
      );
    } else {
      createProductLocation(
        {
          ...values,
          warehouseId: currentWarehouse!.warehouseId,
          status: LocationStatus.AVAILABLE,
        },
        {
          onSuccess: () => {
            toast.success("Thêm vị trí hàng hóa thành công");
            form.resetFields();
            onCancel();
          },
          onError: (error: Error) => {
            toast.error(
              getNotificationMessage(error) || "Thêm vị trí hàng hóa thất bại",
            );
          },
        },
      );
    }
  }

  return (
    <Skeleton loading={isWarehouseLoading} active>
      <Form form={form} layout="vertical" onFinish={handleFinish}>
        <div className="flex items-center gap-8">
          <Form.Item
            className="flex-1"
            label="Tên kệ hàng"
            name="rackName"
            rules={[{ required: true, message: "Vui lòng nhập tên kệ hàng" }]}
          >
            <Input placeholder="Tên kệ hàng" />
          </Form.Item>
          <Form.Item
            className="flex-1"
            label="Loại kệ hàng"
            name="rackType"
            rules={[{ required: true, message: "Vui lòng chọn loại kệ hàng" }]}
          >
            <Select
              placeholder="Chọn loại kệ hàng"
              options={Object.values(RackType).map((rackType: RackType) => ({
                label: RACK_TYPE_NAME[rackType],
                value: rackType,
              }))}
            />
          </Form.Item>
        </div>
        <div className="flex items-center gap-8">
          <Form.Item
            className="flex-1"
            label="Ngăn (1-100)"
            name="rowNumber"
            rules={[{ required: true, message: "Vui lòng nhập ngăn" }]}
          >
            <InputNumber
              className="w-full"
              min={1}
              max={100}
              placeholder="Ngăn"
            />
          </Form.Item>
          <Form.Item
            className="flex-1"
            label="Tầng (1-10)"
            name="columnNumber"
            rules={[{ required: true, message: "Vui lòng nhập tầng" }]}
          >
            <InputNumber
              className="w-full"
              min={1}
              max={10}
              placeholder="Tầng"
            />
          </Form.Item>
        </div>
        {productLocationToUpdate && (
          <div className="flex items-center gap-8">
            <Form.Item
              style={{ width: "calc(50% - 1rem)" }}
              label="Trạng thái"
              name="status"
            >
              <Select
                className="w-full"
                placeholder="Chọn trạng thái"
                options={Object.values(LocationStatus).map((status) => ({
                  label: LOCATION_STATUS_NAME[status],
                  value: status,
                }))}
              />
            </Form.Item>
          </div>
        )}
        <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
          <Space>
            <Button onClick={onCancel} loading={isCreating || isUpdating}>
              Hủy
            </Button>
            <Button
              type="primary"
              htmlType="submit"
              loading={isCreating || isUpdating}
            >
              {productLocationToUpdate ? "Cập nhật" : "Thêm mới"}
            </Button>
          </Space>
        </Form.Item>
      </Form>
    </Skeleton>
  );
};

export default UpdateProductLocationForm;
