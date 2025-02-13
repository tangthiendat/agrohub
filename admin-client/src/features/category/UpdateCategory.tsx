import { Modal } from "antd";
import { useState } from "react";
import EditIcon from "../../common/components/icons/EditIcon";
import UpdateCategoryForm from "./UpdateCategoryForm";
import { ICategory } from "../../interfaces";

interface UpdateCategoryProps {
  category: ICategory;
}

const UpdateCategory: React.FC<UpdateCategoryProps> = ({ category }) => {
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
        width="30%"
        title={<span className="text-lg">Chỉnh sửa loại sản phẩm</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateCategoryForm
          categoryToUpdate={category}
          onCancel={handleCloseModal}
        />
      </Modal>
    </>
  );
};

export default UpdateCategory;
