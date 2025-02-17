import { Button, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useState } from "react";
import UpdateUnitForm from "./UpdateUnitForm";

const AddUnit: React.FC = () => {
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
        width="30%"
        title={<span className="text-lg">Thêm đơn vị tính</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateUnitForm onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default AddUnit;
