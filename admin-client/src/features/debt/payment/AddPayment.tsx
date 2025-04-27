import { useState } from "react";
import { ISupplier } from "../../../interfaces";
import { Button, Modal } from "antd";
import { MdAttachMoney } from "react-icons/md";
import NewPaymentForm from "./NewPaymentForm";

interface AddPaymentProps {
  supplier: ISupplier;
}

const AddPayment: React.FC<AddPaymentProps> = ({ supplier }) => {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);

  const handleOpenModal = () => {
    setIsOpenModal(true);
  };

  const handleCloseModal = () => {
    setIsOpenModal(false);
  };
  return (
    <>
      <Button type="primary" icon={<MdAttachMoney />} onClick={handleOpenModal}>
        Lập phiếu chi
      </Button>
      <Modal
        open={isOpenModal}
        width="70%"
        title={<span className="text-lg">Lập phiếu chi</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <NewPaymentForm supplier={supplier} onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default AddPayment;
