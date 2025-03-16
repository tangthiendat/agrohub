import { Button, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useState } from "react";
import UpdateProductLocationForm from "./UpdateProductLocationForm";

const AddProductLocation: React.FC = () => {
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
        width="40%"
        title={<span className="text-lg">Thêm vị trí hàng hóa</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateProductLocationForm onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default AddProductLocation;
