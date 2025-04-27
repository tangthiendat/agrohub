import { useState } from "react";
import { ICustomer } from "../../../interfaces";
import EditIcon from "../../../common/components/icons/EditIcon";
import { Modal } from "antd";
import UpdateCustomerForm from "./UpdateCustomerForm";

interface UpdateCustomerProps {
  customer: ICustomer;
}

const UpdateCustomer: React.FC<UpdateCustomerProps> = ({ customer }) => {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);

  const handleOpenModal = () => {
    setIsOpenModal(true);
  };

  const handleCloseModal = () => {
    setIsOpenModal(false);
  };

  return (
    <>
      <EditIcon onClick={handleOpenModal} tooltipTitle="Chỉnh sửa" />
      <Modal
        open={isOpenModal}
        width="60%"
        title={<span className="text-lg">Chỉnh sửa khách hàng</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateCustomerForm
          customerToUpdate={customer}
          onCancel={handleCloseModal}
        />
      </Modal>
    </>
  );
};

export default UpdateCustomer;
