import { Modal } from "antd";
import { useState } from "react";
import ViewIcon from "../../../common/components/icons/ViewIcon";
import UpdateBatchLocationForm from "./UpdateBatchLocationForm";
import { IProductBatch } from "../../../interfaces";

interface ViewBatchLocationProps {
  productBatch: IProductBatch;
}

const ViewBatchLocation: React.FC<ViewBatchLocationProps> = ({
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
      <ViewIcon onClick={handleOpenModal} tooltipTitle="Xem chi tiết" />
      <Modal
        open={isOpenModal}
        width="60%"
        title={<span className="text-lg"> Chi tiết lô hàng</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateBatchLocationForm
          productBatch={productBatch}
          onCancel={handleCloseModal}
          viewOnly
        />
      </Modal>
    </>
  );
};

export default ViewBatchLocation;
