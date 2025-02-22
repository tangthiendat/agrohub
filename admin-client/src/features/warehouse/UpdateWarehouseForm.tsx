import { useMutation, useQueryClient } from "@tanstack/react-query";
import { IWarehouse } from "../../interfaces";
import { Button, Form, Input, Space } from "antd";
import { warehouseService } from "../../services";
import toast from "react-hot-toast";
import { getNotificationMessage } from "../../utils/notification";

interface UpdateWarehouseFormProps {
  warehouseToUpdate?: IWarehouse;
  onCancel: () => void;
}

const UpdateWarehouseForm: React.FC<UpdateWarehouseFormProps> = ({
  warehouseToUpdate,
  onCancel,
}) => {
  const [form] = Form.useForm<IWarehouse>();
  const queryClient = useQueryClient();
  const { mutate: createWarehouse, isPending: isCreating } = useMutation({
    mutationFn: warehouseService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["warehouses"],
      });
    },
  });

  function handleFinish(values: IWarehouse) {
    if (warehouseToUpdate) {
      console.log("Updating warehouse", values);
    } else {
      createWarehouse(values, {
        onSuccess: () => {
          toast.success("Thêm kho thành công");
          form.resetFields();
          onCancel();
        },
        onError: (error: Error) => {
          toast.error(getNotificationMessage(error) || "Thêm kho thất bại");
        },
      });
    }
  }

  return (
    <Form form={form} onFinish={handleFinish} layout="vertical">
      <Form.Item
        label="Tên kho"
        name="warehouseName"
        rules={[
          {
            required: true,
            message: "Vui lòng nhập tên kho",
          },
        ]}
      >
        <Input />
      </Form.Item>

      <Form.Item label="Địa chỉ" name="address">
        <Input />
      </Form.Item>

      <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
        <Space>
          <Button onClick={onCancel} loading={isCreating}>
            Hủy
          </Button>
          <Button type="primary" htmlType="submit" loading={isCreating}>
            {warehouseToUpdate ? "Cập nhật" : "Thêm mới"}
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UpdateWarehouseForm;
