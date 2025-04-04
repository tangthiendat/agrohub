import { Button, Modal } from "antd";
import { useState } from "react";
import { PlusOutlined } from "@ant-design/icons";
import UpdateCategoryForm from "./UpdateCategoryForm";

const AddCategory: React.FC = () => {
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
        title={<span className="text-lg">Thêm danh mục sản phẩm</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateCategoryForm onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default AddCategory;
