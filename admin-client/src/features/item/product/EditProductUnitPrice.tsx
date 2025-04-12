import { useEffect, useState } from "react";
import { FormInstance, Input, Modal } from "antd";
import EditIcon from "../../../common/components/icons/EditIcon";
import UpdateProductUnitPriceForm from "./UpdateProductUnitPriceForm";
import { useProductUnitPrice } from "../../../context/ProductUnitPriceContext";
import { IProduct, IProductUnit } from "../../../interfaces";
import { formatCurrency } from "../../../utils/number";
import ViewProductUnitPrices from "./ViewProductUnitPrices";

interface EditProductUnitPriceProps {
  productForm: FormInstance<IProduct>;
  productUnitIndex: number;
  viewOnly: boolean;
  isUpdate?: boolean;
}

const EditProductUnitPrice: React.FC<EditProductUnitPriceProps> = ({
  productForm,
  productUnitIndex,
  viewOnly,
  isUpdate,
}) => {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);
  const { currentProductUnitPrice, setCurrentProductUnitPrice } =
    useProductUnitPrice();

  useEffect(() => {
    if (viewOnly || isUpdate) {
      const productUnit: IProductUnit = productForm.getFieldValue([
        "productUnits",
        productUnitIndex,
      ]);
      const currentProductUnitPrice =
        productUnit?.productUnitPrices &&
        productUnit.productUnitPrices.length > 0
          ? productUnit.productUnitPrices.find(
              (pup) => pup.validTo === undefined,
            )
          : undefined;
      setCurrentProductUnitPrice(currentProductUnitPrice);
    }
  }, [
    viewOnly,
    isUpdate,
    productForm,
    productUnitIndex,
    setCurrentProductUnitPrice,
  ]);

  const handleOpenModal = () => {
    setIsOpenModal(true);
  };

  const handleCloseModal = () => {
    setIsOpenModal(false);
  };
  return (
    <div className="flex items-center gap-4">
      {currentProductUnitPrice?.price && (
        <Input
          readOnly
          value={formatCurrency(currentProductUnitPrice?.price)}
          addonAfter="VND"
        />
      )}

      {!viewOnly && (
        <EditIcon onClick={handleOpenModal} tooltipTitle="Chỉnh sửa giá" />
      )}

      {currentProductUnitPrice && (
        <ViewProductUnitPrices
          key={productUnitIndex}
          productForm={productForm}
          productUnitIndex={productUnitIndex}
        />
      )}

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
