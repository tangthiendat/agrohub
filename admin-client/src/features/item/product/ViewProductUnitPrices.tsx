import { FormInstance, Modal } from "antd";
import { IProduct } from "../../../interfaces";
import { useState } from "react";
import ProductUnitPriceTable from "./ProductUnitPriceTable";
import ViewIcon from "../../../common/components/icons/ViewIcon";

interface ViewProductUnitPricesProps {
  productForm: FormInstance<IProduct>;
  productUnitIndex: number;
}

const ViewProductUnitPrices: React.FC<ViewProductUnitPricesProps> = ({
  productForm,
  productUnitIndex,
}) => {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);
  const handleOpenModal = () => {
    setIsOpenModal(true);
  };

  const handleCloseModal = () => {
    setIsOpenModal(false);
  };
  return (
    <>
      <ViewIcon onClick={handleOpenModal} tooltipTitle="Xem chi tiết giá" />
      <Modal
        open={isOpenModal}
        width="40%"
        title={<span className="text-lg">Chi tiết giá đơn vị tính</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <ProductUnitPriceTable
          productForm={productForm}
          productUnitIndex={productUnitIndex}
        />
      </Modal>
    </>
  );
};

export default ViewProductUnitPrices;
