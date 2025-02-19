import { Button, Form, Input, Space, Switch } from "antd";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { ISupplier } from "../../interfaces";
import { supplierService } from "../../services";
import { getNotificationMessage } from "../../utils/notification";

interface UpdateSupplierFormProps {
  supplierToUpdate?: ISupplier;
  onCancel: () => void;
  viewOnly?: boolean;
}

const UpdateSupplierForm: React.FC<UpdateSupplierFormProps> = ({
  supplierToUpdate,
  onCancel,
  viewOnly,
}) => {
  const [form] = Form.useForm<ISupplier>();
  const queryClient = useQueryClient();

  const { mutate: createSupplier, isPending: isCreating } = useMutation({
    mutationFn: supplierService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["suppliers"],
      });
    },
  });

  function handleFinish(values: ISupplier) {
    if (supplierToUpdate) {
      console.log("Updating supplier", values);
    } else {
      createSupplier(values, {
        onSuccess: () => {
          toast.success("Thêm nhà cung cấp thành công");
          form.resetFields();
          onCancel();
        },
        onError: (error: Error) => {
          toast.error(
            getNotificationMessage(error) || "Thêm nhà cung cấp thất bại.",
          );
        },
      });
    }
  }
  return (
    <Form
      form={form}
      onFinish={handleFinish}
      layout="vertical"
      initialValues={{ active: true }}
    >
      <div className="flex gap-8">
        <Form.Item
          className="flex-1"
          label="Tên nhà cung cấp"
          name="supplierName"
          rules={[
            {
              required: true,
              message: "Vui lòng nhập tên nhà cung cấp",
            },
          ]}
        >
          <Input placeholder="Tên nhà cung cấp" readOnly={viewOnly} />
        </Form.Item>
        <Form.Item
          className="flex-1"
          label="Trạng thái"
          name="active"
          valuePropName="checked"
        >
          <Switch checkedChildren="ACTIVE" unCheckedChildren="INACTIVE" />
        </Form.Item>
      </div>
      <div className="flex gap-8">
        <Form.Item
          className="flex-1"
          label="Email"
          name="email"
          rules={[
            {
              required: true,
              message: "Vui lòng nhập email",
            },
            {
              type: "email",
              message: "Email không hợp lệ",
            },
          ]}
        >
          <Input placeholder="Email" readOnly={viewOnly} />
        </Form.Item>

        <Form.Item
          className="flex-1"
          label="Số điện thoại"
          name="phoneNumber"
          rules={[
            {
              required: true,
              message: "Vui lòng nhập số điện thoại",
            },
            {
              pattern: /(84|0[3|5|7|8|9])+([0-9]{8})\b/,
              message: "Số điện thoại không hợp lệ",
            },
          ]}
        >
          <Input placeholder="Số điện thoại" readOnly={viewOnly} />
        </Form.Item>
      </div>
      <div className="flex gap-8">
        <Form.Item className="flex-1" label="Địa chỉ" name="address">
          <Input placeholder="Địa chỉ" readOnly={viewOnly} />
        </Form.Item>
        <Form.Item className="flex-1" label="Mã số thuế" name="taxCode">
          <Input placeholder="Mã số thuế" readOnly={viewOnly} />
        </Form.Item>
      </div>
      <div className="flex gap-8">
        <Form.Item
          className="flex-1"
          label="Người liên hệ"
          name="contactPerson"
        >
          <Input placeholder="Người liên hệ" readOnly={viewOnly} />
        </Form.Item>
        <Form.Item className="flex-1" label="Ghi chú" name="notes">
          <Input.TextArea placeholder="Ghi chú" readOnly={viewOnly} rows={2} />
        </Form.Item>
      </div>
      <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
        <Space>
          <Button onClick={onCancel} loading={isCreating}>
            Hủy
          </Button>
          <Button type="primary" htmlType="submit" loading={isCreating}>
            {supplierToUpdate ? "Cập nhật" : "Thêm mới"}
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UpdateSupplierForm;
