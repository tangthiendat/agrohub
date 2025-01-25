import { useState } from "react";
import { IRole } from "../../../interfaces";
import ViewDetailsIcon from "../../../common/components/icons/ViewDetailsIcon";
import { Modal } from "antd";
import UpdateRoleForm from "./UpdateRoleForm";

interface Props {
  role: IRole;
}

const ViewRole: React.FC<Props> = ({ role }) => {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);

  const handleOpenModal = () => {
    setIsOpenModal(true);
  };

  const handleCloseModal = () => {
    setIsOpenModal(false);
  };
  return (
    <>
      <ViewDetailsIcon onClick={handleOpenModal} />
      <Modal
        open={isOpenModal}
        width="75%"
        title={<span className="text-lg">Xem thông tin vai trò</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateRoleForm
          roleToUpdate={role}
          onCancel={handleCloseModal}
          viewOnly
        />
      </Modal>
    </>
  );
};

export default ViewRole;
