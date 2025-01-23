import toast from "react-hot-toast";
import { useEffect } from "react";
import { Button, Col, Form, Input, Row, Select, Space } from "antd";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { HttpMethod, Module } from "../../../common/enums";
import { IPermission } from "../../../interfaces";
import { permissionService } from "../../../services";

interface Props {
  permissionToUpdate?: IPermission;
  onCancel: () => void;
}

interface UpdatePermissionArgs {
  permissionId: number;
  updatedPermission: IPermission;
}

const methodOptions = Object.values(HttpMethod).map((httpMethod: string) => ({
  value: httpMethod,
  label: httpMethod,
}));

const moduleOptions = Object.values(Module).map((module: string) => ({
  value: module,
  label: module,
}));

const UpdatePermissionForm: React.FC<Props> = ({
  permissionToUpdate,
  onCancel,
}) => {
  const [form] = Form.useForm<IPermission>();
  const queryClient = useQueryClient();

  const { mutate: createPermission, isPending: isCreating } = useMutation({
    mutationFn: permissionService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey.includes("permissions");
        },
      });
    },
  });

  const { mutate: updatePermission, isPending: isUpdating } = useMutation({
    mutationFn: ({ permissionId, updatedPermission }: UpdatePermissionArgs) =>
      permissionService.update(permissionId, updatedPermission),
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey[0] === "permissions";
        },
      });
    },
  });

  function handleFinish(values: IPermission) {
    if (permissionToUpdate) {
      updatePermission(
        {
          permissionId: permissionToUpdate.permissionId,
          updatedPermission: values,
        },
        {
          onSuccess: () => {
            toast.success("Cập nhật quyền hạn thành công");
            onCancel();
            form.resetFields();
          },
          onError: () => {
            toast.error("Cập nhật quyền hạn thất bại");
          },
        },
      );
    } else {
      createPermission(values, {
        onSuccess: () => {
          toast.success("Thêm quyền hạn thành công");
          onCancel();
          form.resetFields();
        },
        onError: () => {
          toast.error("Thêm quyền hạn thất bại");
        },
      });
    }
  }

  useEffect(() => {
    if (permissionToUpdate) {
      form.setFieldsValue(permissionToUpdate);
    }
  }, [form, permissionToUpdate]);

  return (
    <Form onFinish={handleFinish} layout="vertical" form={form}>
      <Row>
        <Col span={24}>
          <Form.Item
            label="Tên quyền hạn"
            name="permissionName"
            rules={[
              {
                required: true,
                message: "Tên quyền hạn không được để trống",
              },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="Đường dẫn API"
            name="apiPath"
            rules={[
              {
                required: true,
                message: "Đường dẫn API không được để trống",
              },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="Phương thức"
            name="httpMethod"
            rules={[
              { required: true, message: "Phương thức không được để trống" },
            ]}
          >
            <Select allowClear options={methodOptions} />
          </Form.Item>
          <Form.Item
            label="Module"
            name="module"
            rules={[{ required: true, message: "Module không được để trống" }]}
          >
            <Select allowClear options={moduleOptions} />
          </Form.Item>
          <Form.Item label="Mô tả" name="description">
            <Input.TextArea />
          </Form.Item>
        </Col>
      </Row>
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
            {permissionToUpdate ? "Cập nhật" : "Thêm mới"}
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UpdatePermissionForm;
