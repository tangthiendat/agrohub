import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Button, Form, Input, InputNumber, Space } from "antd";
import { useEffect } from "react";
import toast from "react-hot-toast";
import Loading from "../../common/components/Loading";
import { useCurrentWarehouse } from "../../common/hooks";
import { ISupplier, ISupplierRating } from "../../interfaces";
import { supplierService } from "../../services";
import { getNotificationMessage } from "../../utils/notification";

interface RateSupplierFormProps {
  supplier: ISupplier;
  onCancel: () => void;
}

interface AddSupplierRatingArgs {
  supplierId: string;
  supplierRating: ISupplierRating;
}

interface UpdateSupplierRatingArgs {
  supplierId: string;
  ratingId: string;
  supplierRating: ISupplierRating;
}

const RateSupplierForm: React.FC<RateSupplierFormProps> = ({
  supplier,
  onCancel,
}) => {
  const [form] = Form.useForm<ISupplierRating>();
  const queryClient = useQueryClient();
  const isUpdateSession = !!supplier.supplierRating;

  useEffect(() => {
    if (supplier.supplierRating) {
      form.setFieldsValue(supplier.supplierRating);
    }
  }, [supplier, form]);

  const { currentWarehouse, isLoading: isWarehouseLoading } =
    useCurrentWarehouse();

  const { mutate: createSupplierRating, isPending: isCreating } = useMutation({
    mutationFn: ({ supplierId, supplierRating }: AddSupplierRatingArgs) =>
      supplierService.createRating(supplierId, supplierRating),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["suppliers"],
      });
    },
  });

  const { mutate: updateSupplierRating } = useMutation({
    mutationFn: ({
      supplierId,
      ratingId,
      supplierRating,
    }: UpdateSupplierRatingArgs) =>
      supplierService.updateRating(supplierId, ratingId, supplierRating),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["suppliers"],
      });
    },
  });

  if (isWarehouseLoading) {
    return <Loading />;
  }

  function handleFinish(values: ISupplierRating) {
    if (isUpdateSession) {
      updateSupplierRating(
        {
          supplierId: supplier.supplierId,
          ratingId: supplier.supplierRating!.ratingId,
          supplierRating: values,
        },
        {
          onSuccess: () => {
            toast.success("Cập nhật đánh giá nhà cung cấp thành công");
            form.resetFields();
            onCancel();
          },
          onError: (error: Error) => {
            toast.error(
              getNotificationMessage(error) ||
                "Cập nhật đánh giá nhà cung cấp thất bại",
            );
          },
        },
      );
    } else {
      createSupplierRating(
        {
          supplierId: supplier.supplierId,
          supplierRating: {
            ...values,
            warehouseId: currentWarehouse!.warehouseId,
          },
        },
        {
          onSuccess: () => {
            toast.success("Đánh giá nhà cung cấp thành công");
            form.resetFields();
            onCancel();
          },
          onError: (error: Error) => {
            toast.error(
              getNotificationMessage(error) || "Đánh giá nhà cung cấp thất bại",
            );
          },
        },
      );
    }
  }

  return (
    <Form
      form={form}
      layout="vertical"
      onFinish={handleFinish}
      initialValues={{ trustScore: 100 }}
    >
      <Form.Item label="Điểm tín nhiệm" name="trustScore">
        <InputNumber className="w-[30%]" min={0} max={100} addonAfter="/100" />
      </Form.Item>
      <Form.Item label="Nhận xét" name="comment">
        <Input.TextArea rows={4} />
      </Form.Item>
      <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
        <Space>
          <Button onClick={onCancel} loading={isCreating}>
            Hủy
          </Button>
          <Button type="primary" htmlType="submit" loading={isCreating}>
            {isUpdateSession ? "Cập nhật" : "Đánh giá"}
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default RateSupplierForm;
