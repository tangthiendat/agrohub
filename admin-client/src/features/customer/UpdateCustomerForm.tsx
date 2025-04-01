import { useEffect } from "react";
import { Button, Form, Input, Select, Space, Switch } from "antd";
import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { ICustomer } from "../../interfaces";
import { CustomerType } from "../../common/enums";
import { CUSTOMER_TYPE_NAME } from "../../common/constants";
import { customerService } from "../../services";
import { getNotificationMessage } from "../../utils/notification";

interface UpdateCustomerFormProps {
  customerToUpdate?: ICustomer;
  onCancel: () => void;
}

interface UpdateCustomerArgs {
  customerId: string;
  updatedCustomer: ICustomer;
}

const UpdateCustomerForm: React.FC<UpdateCustomerFormProps> = ({
  customerToUpdate,
  onCancel,
}) => {
  const [form] = Form.useForm<ICustomer>();
  const queryClient = useQueryClient();

  const { mutate: createCustomer, isPending: isCreating } = useMutation({
    mutationFn: customerService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["customers"],
      });
    },
  });

  const { mutate: updateSupplier, isPending: isUpdating } = useMutation({
    mutationFn: ({ customerId, updatedCustomer }: UpdateCustomerArgs) =>
      customerService.update(customerId, updatedCustomer),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["customers"],
      });
    },
  });

  useEffect(() => {
    if (customerToUpdate) {
      form.setFieldsValue(customerToUpdate);
    }
  }, [customerToUpdate, form]);

  function handleFinish(values: ICustomer) {
    if (customerToUpdate) {
      updateSupplier(
        {
          customerId: customerToUpdate.customerId,
          updatedCustomer: values,
        },
        {
          onSuccess: () => {
            toast.success("Cập nhật khách hàng thành công");
            form.resetFields();
            onCancel();
          },
          onError: (error: Error) => {
            toast.error(
              getNotificationMessage(error) || "Cập nhật khách hàng thất bại",
            );
          },
        },
      );
    } else {
      createCustomer(values, {
        onSuccess: () => {
          toast.success("Thêm khách hàng thành công");
          form.resetFields();
          onCancel();
        },
        onError: (error: Error) => {
          toast.error(
            getNotificationMessage(error) || "Thêm khách hàng thất bại.",
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
          label="Tên khách hàng"
          name="customerName"
          rules={[
            {
              required: true,
              message: "Vui lòng nhập tên khách hàng",
            },
          ]}
        >
          <Input placeholder="Tên khách hàng" />
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
          label="Loại khách hàng"
          name="customerType"
          rules={[
            {
              required: true,
              message: "Vui lòng chọn loại khách hàng",
            },
          ]}
        >
          <Select
            options={Object.values(CustomerType).map(
              (customerType: string) => ({
                label: CUSTOMER_TYPE_NAME[customerType],
                value: customerType,
              }),
            )}
            placeholder="Chọn loại khách hàng"
          />
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
          <Input placeholder="Số điện thoại" />
        </Form.Item>
      </div>
      <div className="flex gap-8">
        <Form.Item
          className="flex-1"
          label="Email"
          name="email"
          rules={[
            {
              type: "email",
              message: "Email không hợp lệ",
            },
          ]}
        >
          <Input placeholder="Email" />
        </Form.Item>
        <Form.Item className="flex-1" label="Mã số thuế" name="taxCode">
          <Input placeholder="Mã số thuế" />
        </Form.Item>
      </div>
      <div className="flex gap-8">
        <Form.Item className="flex-1" label="Địa chỉ" name="address">
          <Input.TextArea placeholder="Địa chỉ" rows={2} />
        </Form.Item>
        <Form.Item className="flex-1" label="Ghi chú" name="notes">
          <Input.TextArea placeholder="Ghi chú" rows={2} />
        </Form.Item>
      </div>
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
            {customerToUpdate ? "Cập nhật" : "Thêm mới"}
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UpdateCustomerForm;
