import { useState } from "react";
import { ICustomer } from "../../../interfaces";
import { MdAttachMoney } from "react-icons/md";
import { Button, Modal } from "antd";
import NewReceiptForm from "./NewReceiptForm";

interface AddReceiptProps {
  customer: ICustomer;
}

const AddReceipt: React.FC<AddReceiptProps> = ({ customer }) => {
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
        Lập phiếu thu
      </Button>
      <Modal
        open={isOpenModal}
        width="70%"
        title={<span className="text-lg">Lập phiếu thu</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <NewReceiptForm customer={customer} onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default AddReceipt;
