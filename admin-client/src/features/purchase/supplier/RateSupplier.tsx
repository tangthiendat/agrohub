import { Modal } from "antd";
import { useState } from "react";
import CommentIcon from "../../../common/components/icons/CommentIcon";
import RateSupplierForm from "./RateSupplierForm";
import { ISupplier } from "../../../interfaces";

interface RateSupplierProps {
  supplier: ISupplier;
}

const RateSupplier: React.FC<RateSupplierProps> = ({ supplier }) => {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);

  const handleOpenModal = () => {
    setIsOpenModal(true);
  };

  const handleCloseModal = () => {
    setIsOpenModal(false);
  };
  return (
    <>
      <CommentIcon tooltipTitle="Đánh giá" onClick={handleOpenModal} />
      <Modal
        open={isOpenModal}
        width="30%"
        title={<span className="text-lg">Thêm nhà cung cấp</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <RateSupplierForm supplier={supplier} onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default RateSupplier;
