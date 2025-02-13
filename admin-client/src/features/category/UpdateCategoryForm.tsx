import toast from "react-hot-toast";
import { Button, Col, Form, Input, Row, Space } from "antd";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { categoryService } from "../../services";
import { ICategory } from "../../interfaces";
import { useEffect } from "react";

interface UpdateCategoryFormProps {
  categoryToUpdate?: ICategory;
  onCancel: () => void;
}

interface UpdateCategoryArgs {
  categoryId: number;
  updatedCategory: ICategory;
}

const UpdateCategoryForm: React.FC<UpdateCategoryFormProps> = ({
  categoryToUpdate,
  onCancel,
}) => {
  const [form] = Form.useForm<ICategory>();
  const queryClient = useQueryClient();

  const { mutate: createCategory, isPending: isCreating } = useMutation({
    mutationFn: categoryService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey.includes("categories");
        },
      });
    },
  });

  const { mutate: updateCategory, isPending: isUpdating } = useMutation({
    mutationFn: ({ categoryId, updatedCategory }: UpdateCategoryArgs) =>
      categoryService.update(categoryId, updatedCategory),
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey.includes("categories");
        },
      });
    },
  });

  useEffect(() => {
    if (categoryToUpdate) {
      form.setFieldsValue(categoryToUpdate);
    }
  }, [form, categoryToUpdate]);

  function handleFinish(values: ICategory) {
    if (categoryToUpdate) {
      updateCategory(
        {
          categoryId: categoryToUpdate.categoryId,
          updatedCategory: values,
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
      createCategory(values, {
        onSuccess: () => {
          toast.success("Thêm loại sản phẩm thành công");
          onCancel();
          form.resetFields();
        },
        onError: () => {
          toast.error("Thêm loại sản phẩm thất bại");
        },
      });
    }
  }

  return (
    <Form onFinish={handleFinish} layout="vertical" form={form}>
      <Row>
        <Col span={24}>
          <Form.Item
            label="Tên loại sản phẩm"
            name="categoryName"
            rules={[
              {
                required: true,
                message: "Tên loại sản phẩm không được để trống",
              },
            ]}
          >
            <Input />
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
            {categoryToUpdate ? "Cập nhật" : "Thêm mới"}
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UpdateCategoryForm;
