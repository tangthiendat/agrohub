import { Modal } from "antd";
import { useState } from "react";
import EditIcon from "../../../common/components/icons/EditIcon";
import UpdateUnitForm from "./UpdateUnitForm";
import { IUnit } from "../../../interfaces";

interface UpdateUnitProps {
  unit: IUnit;
}

const UpdateUnit: React.FC<UpdateUnitProps> = ({ unit }) => {
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
        title={<span className="text-lg">Chỉnh sửa đơn vị tính</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateUnitForm unitToUpdate={unit} onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default UpdateUnit;
