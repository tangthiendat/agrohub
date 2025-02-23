import { useState } from "react";
import { IWarehouse } from "../../interfaces";
import EditIcon from "../../common/components/icons/EditIcon";
import { Modal } from "antd";
import UpdateWarehouseForm from "./UpdateWarehouseForm";

interface UpdateWarehouseProps {
  warehouse: IWarehouse;
}

const UpdateWarehouse: React.FC<UpdateWarehouseProps> = ({ warehouse }) => {
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
        title={<span className="text-lg">Chỉnh sửa thông tin kho hàng</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateWarehouseForm
          warehouseToUpdate={warehouse}
          onCancel={handleCloseModal}
        />
      </Modal>
    </>
  );
};

export default UpdateWarehouse;
