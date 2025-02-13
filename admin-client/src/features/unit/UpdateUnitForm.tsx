import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Button, Form, Input, Space } from "antd";
import toast from "react-hot-toast";
import { IUnit } from "../../interfaces";
import { unitService } from "../../services";
import { useEffect } from "react";

interface UpdateUnitFormProps {
  unitToUpdate?: IUnit;
  onCancel: () => void;
}

interface UpdateUnitArgs {
  unitId: number;
  updatedUnit: IUnit;
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

  const { mutate: updateUnit, isPending: isUpdating } = useMutation({
    mutationFn: ({ unitId, updatedUnit }: UpdateUnitArgs) =>
      unitService.update(unitId, updatedUnit),
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey.includes("units");
        },
      });
    },
  });

  useEffect(() => {
    if (unitToUpdate) {
      form.setFieldsValue(unitToUpdate);
    }
  }, [form, unitToUpdate]);

  function handleFinish(values: IUnit) {
    if (unitToUpdate) {
      updateUnit(
        {
          unitId: unitToUpdate.unitId,
          updatedUnit: values,
        },
        {
          onSuccess: () => {
            toast.success("Cập nhật loại sản phẩm thành công");
            onCancel();
            form.resetFields();
          },
          onError: () => {
            toast.error("Cập nhật loại sản phẩm thất bại");
          },
        },
      );
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
          <Button onClick={onCancel} loading={isCreating || isUpdating}>
            Hủy
          </Button>
          <Button
            type="primary"
            htmlType="submit"
            loading={isCreating || isUpdating}
          >
            {unitToUpdate ? "Cập nhật" : "Thêm mới"}
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UpdateUnitForm;
