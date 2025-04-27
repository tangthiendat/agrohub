import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { Button, Form, Modal, Space, Tooltip } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import SearchSupplierBar from "../../purchase/supplier/SearchSupplierBar";
import UpdateSupplierForm from "../../purchase/supplier/UpdateSupplierForm";
import { IProduct, ISupplier, ISupplierProduct } from "../../../interfaces";
import { supplierProductService } from "../../../services";
import { getNotificationMessage } from "../../../utils/notification";

interface UpdateProductSupplierFormProps {
  product: IProduct;
}

const UpdateProductSupplierForm: React.FC<UpdateProductSupplierFormProps> = ({
  product,
}) => {
  const [form] = Form.useForm<ISupplierProduct>();
  const queryClient = useQueryClient();

  const [isOpenSupplierModal, setIsOpenSupplierModal] =
    useState<boolean>(false);

  const openSupplierModal = () => {
    setIsOpenSupplierModal(true);
  };

  const closeSupplierModal = () => {
    setIsOpenSupplierModal(false);
  };

  const { mutate: createSupplierProduct, isPending: isCreating } = useMutation({
    mutationFn: supplierProductService.create,
    onSuccess: () => {
      form.resetFields();
    },
  });

  function handleSelectSupplier(supplier: ISupplier) {
    form.setFieldsValue({
      productId: product.productId,
      supplier: {
        supplierId: supplier.supplierId,
      },
    });
  }

  function handleFinish() {
    const supplierProduct = form.getFieldsValue(true);
    createSupplierProduct(supplierProduct, {
      onSuccess: () => {
        toast.success("Thêm nhà cung cấp thành công");
        queryClient.invalidateQueries({
          queryKey: ["suppliers", product.productId],
        });
      },
      onError: (error: Error) => {
        toast.error(
          getNotificationMessage(error) || "Thêm nhà cung cấp thất bại",
        );
      },
    });
  }

  return (
    <Form form={form} onFinish={handleFinish}>
      <div className="flex items-center gap-4">
        <Space.Compact className="w-[40%]">
          <SearchSupplierBar
            className="w-full"
            placeholder="Nhập ID, tên, hoặc số điện thoại nhà cung cấp"
            onSelect={handleSelectSupplier}
          />
          <Tooltip title="Thêm nhà cung cấp mới">
            <Button icon={<PlusOutlined onClick={openSupplierModal} />} />
          </Tooltip>
          <Modal
            open={isOpenSupplierModal}
            width="60%"
            title={<span className="text-lg">Thêm nhà cung cấp</span>}
            destroyOnClose
            onCancel={closeSupplierModal}
            footer={null}
          >
            <UpdateSupplierForm onCancel={closeSupplierModal} />
          </Modal>
        </Space.Compact>

        <Button type="primary" htmlType="submit" loading={isCreating}>
          Thêm vào danh sách
        </Button>
      </div>
    </Form>
  );
};

export default UpdateProductSupplierForm;
