import { useState } from "react";
import { Modal } from "antd";
import UpdateUserForm from "./UpdateUserForm";
import EditIcon from "../../../common/components/icons/EditIcon";
import { IUser } from "../../../interfaces";

interface UpdateUserProps {
  user: IUser;
}

const UpdateUser: React.FC<UpdateUserProps> = ({ user }) => {
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
        width="50%"
        title={<span className="text-lg">Chỉnh sửa người dùng</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdateUserForm userToUpdate={user} onCancel={handleCloseModal} />
      </Modal>
    </>
  );
};

export default UpdateUser;
