import { Modal } from "antd";
import { useState } from "react";
import EditIcon from "../../common/components/icons/EditIcon";
import UpdateProductLocationForm from "./UpdateProductLocationForm";
import { IProductLocation } from "../../interfaces";

interface UpdateProductLocationProps {
  productLocation: IProductLocation;
}

const UpdateProductLocation: React.FC<UpdateProductLocationProps> = ({
  productLocation,
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
      <EditIcon onClick={handleOpenModal} tooltipTitle="Chỉnh sửa" />
      <Modal
        open={isOpenModal}
        width="40%"
        title={<span className="text-lg">Chỉnh sửa vị trí sản phẩm</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateProductLocationForm
          productLocationToUpdate={productLocation}
          onCancel={handleCloseModal}
        />
      </Modal>
    </>
  );
};

export default UpdateProductLocation;
