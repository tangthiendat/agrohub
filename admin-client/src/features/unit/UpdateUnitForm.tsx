import { Button, Col, Form, Input, Row, Space } from "antd";
import { IRole, IUnit } from "../../interfaces";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { unitService } from "../../services/product/unit-service";
import toast from "react-hot-toast";

interface UpdateUnitFormProps {
  unitToUpdate?: IUnit;
  onCancel: () => void;
}

const UpdateUnitForm: React.FC<UpdateUnitFormProps> = ({
  unitToUpdate,
  onCancel,
}) => {
  const [form] = Form.useForm<IUnit>();
  const queryClient = useQueryClient();

  const { mutate: createUnit, isPending: isCreating } = useMutation({
    mutationFn: unitService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey.includes("units");
        },
      });
    },
  });

  function handleFinish(values: IUnit) {
    if (unitToUpdate) {
      // updateCategory(
      //   {
      //     categoryId: categoryToUpdate.categoryId,
      //     updatedCategory: values,
      //   },
      //   {
      //     onSuccess: () => {
      //       toast.success("Cập nhật loại sản phẩm thành công");
      //       onCancel();
      //       form.resetFields();
      //     },
      //     onError: () => {
      //       toast.error("Cập nhật loại sản phẩm thất bại");
      //     },
      //   },
      // );
    } else {
      createUnit(values, {
        onSuccess: () => {
          toast.success("Thêm đơn vị tính thành công");
          onCancel();
          form.resetFields();
        },
        onError: () => {
          toast.error("Thêm đơn vị tính thất bại");
        },
      });
    }
  }

  return (
    <Form onFinish={handleFinish} layout="vertical" form={form}>
      <Form.Item
        label="Tên đơn vị tính"
        name="unitName"
        rules={[
          {
            required: true,
            message: "Tên đơn vị tính không được để trống",
          },
        ]}
      >
        <Input />
      </Form.Item>

      <Form.Item label="Mô tả" name="description">
        <Input.TextArea rows={2} />
      </Form.Item>

      <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
        <Space>
          <Button onClick={onCancel} loading={isCreating}>
            Hủy
          </Button>
          <Button type="primary" htmlType="submit" loading={isCreating}>
            {unitToUpdate ? "Cập nhật" : "Thêm mới"}
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UpdateUnitForm;
