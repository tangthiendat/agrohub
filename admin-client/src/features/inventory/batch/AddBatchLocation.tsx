import { useState } from "react";
import { IProductBatch } from "../../../interfaces";
import AddLocationIcon from "../../../common/components/icons/AddLocationIcon";
import { Modal } from "antd";
import UpdateBatchLocationForm from "./UpdateBatchLocationForm";

interface AddBatchLocationProps {
  productBatch: IProductBatch;
}

const AddBatchLocation: React.FC<AddBatchLocationProps> = ({
  productBatch,
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
      <AddLocationIcon onClick={handleOpenModal} tooltipTitle="Thêm vị trí" />
      <Modal
        open={isOpenModal}
        width="60%"
        title={<span className="text-lg">Thêm vị trí lô hàng</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateBatchLocationForm
          productBatch={productBatch}
          onCancel={handleCloseModal}
        />
      </Modal>
    </>
  );
};

export default AddBatchLocation;
