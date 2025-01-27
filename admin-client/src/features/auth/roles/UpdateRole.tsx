import { useState } from "react";
import { Modal } from "antd";
import EditIcon from "../../../common/components/icons/EditIcon";
import UpdateRoleForm from "./UpdateRoleForm";
import { IRole } from "../../../interfaces";

interface UpdateRoleProps {
  role: IRole;
}

const UpdateRole: React.FC<UpdateRoleProps> = ({ role }) => {
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
        width="75%"
        title={<span className="text-lg">Chỉnh sửa quyền hạn</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateRoleForm roleToUpdate={role} onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default UpdateRole;
