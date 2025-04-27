import { Button, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useState } from "react";
import UpdateCustomerForm from "./UpdateCustomerForm";

const AddCustomer: React.FC = () => {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);

  const handleOpenModal = () => {
    setIsOpenModal(true);
  };

  const handleCloseModal = () => {
    setIsOpenModal(false);
  };
  return (
    <>
      <Button type="primary" icon={<PlusOutlined />} onClick={handleOpenModal}>
        Thêm mới
      </Button>
      <Modal
        open={isOpenModal}
        width="60%"
        title={<span className="text-lg">Thêm khách hàng</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateCustomerForm onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default AddCustomer;
