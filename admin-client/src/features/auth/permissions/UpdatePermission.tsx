import { useState } from "react";
import { IPermission } from "../../../interfaces";
import EditIcon from "../../../common/components/icons/EditIcon";
import { Modal } from "antd";
import UpdatePermissionForm from "./UpdatePermissionForm";

interface Props {
  permission: IPermission;
}

const UpdatePermission: React.FC<Props> = ({ permission }) => {
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
        title={<span className="text-lg">Chỉnh sửa quyền hạn</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <UpdatePermissionForm
          permissionToUpdate={permission}
          onCancel={handleCloseModal}
        />
      </Modal>
    </>
  );
};

export default UpdatePermission;
