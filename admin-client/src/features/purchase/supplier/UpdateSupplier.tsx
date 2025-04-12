import { Modal } from "antd";
import { useState } from "react";
import UpdateSupplierForm from "./UpdateSupplierForm";
import EditIcon from "../../../common/components/icons/EditIcon";
import { ISupplier } from "../../../interfaces";

interface UpdateSupplierProps {
  supplier: ISupplier;
}

const UpdateSupplier: React.FC<UpdateSupplierProps> = ({ supplier }) => {
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
        title={<span className="text-lg">Chỉnh sửa nhà cung cấp</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateSupplierForm
          supplierToUpdate={supplier}
          onCancel={handleCloseModal}
        />
      </Modal>
    </>
  );
};

export default UpdateSupplier;
