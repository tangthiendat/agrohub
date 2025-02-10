import { useState } from "react";
import { FormInstance, Input, Modal } from "antd";
import EditIcon from "../../common/components/icons/EditIcon";
import UpdateProductUnitPriceForm from "./UpdateProductUnitPriceForm";
import { useProductUnitPrice } from "../../context/ProductUnitPriceContext";
import { IProduct } from "../../interfaces";
import { formatCurrency } from "../../utils/number";

interface EditProductUnitPriceProps {
  productForm: FormInstance<IProduct>;
  productUnitIndex: number;
}

const EditProductUnitPrice: React.FC<EditProductUnitPriceProps> = ({
  productForm,
  productUnitIndex,
}) => {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);
  const { currentProductUnitPrice } = useProductUnitPrice();

  const handleOpenModal = () => {
    setIsOpenModal(true);
  };

  const handleCloseModal = () => {
    setIsOpenModal(false);
  };
  return (
    <div className="flex items-center gap-4">
      <Input
        readOnly
        value={
          currentProductUnitPrice?.price
            ? formatCurrency(currentProductUnitPrice?.price)
            : ""
        }
        addonAfter="VND"
      />

      <EditIcon onClick={handleOpenModal} tooltipTitle="Chỉnh sửa giá" />
      <Modal
        open={isOpenModal}
        width="25%"
        title={<span className="text-lg">Chỉnh sửa giá</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateProductUnitPriceForm
          productForm={productForm}
          productUnitIndex={productUnitIndex}
          onCancel={handleCloseModal}
        />
      </Modal>
    </div>
  );
};

export default EditProductUnitPrice;
