import { useState } from "react";
import { Modal } from "antd";
import ViewIcon from "../../common/components/icons/ViewIcon";
import DebtAccountInfo from "./DebtAccountInfo";
import { IPartyDebtAccount } from "../../interfaces";

interface ViewDebtAccountProps {
  debtAccount: IPartyDebtAccount;
}

const ViewDebtAccount: React.FC<ViewDebtAccountProps> = ({ debtAccount }) => {
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);

  const handleOpenModal = () => {
    setIsOpenModal(true);
  };

  const handleCloseModal = () => {
    setIsOpenModal(false);
  };
  return (
    <>
      <ViewIcon onClick={handleOpenModal} tooltipTitle="Xem chi tiết" />
      <Modal
        open={isOpenModal}
        width="70%"
        title={<span className="text-lg">Chi tiết công nợ</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <DebtAccountInfo debtAccount={debtAccount} />
      </Modal>
    </>
  );
};

export default ViewDebtAccount;
