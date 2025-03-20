import { useState } from "react";
import { IImportInvoice } from "../../interfaces";
import ViewIcon from "../../common/components/icons/ViewIcon";
import { Modal } from "antd";
import ImportInvoiceInfo from "./ImportInvoiceInfo";

interface ViewImportInvoiceProps {
  importInvoice: IImportInvoice;
}

const ViewImportInvoice: React.FC<ViewImportInvoiceProps> = ({
  importInvoice,
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
      <ViewIcon onClick={handleOpenModal} tooltipTitle="Xem chi tiết" />
      <Modal
        open={isOpenModal}
        width="70%"
        title={<span className="text-lg">Thông tin phiếu nhập</span>}
        destroyOnClose
        onCancel={handleCloseModal}
        footer={null}
      >
        <ImportInvoiceInfo importInvoice={importInvoice} />
      </Modal>
    </>
  );
};

export default ViewImportInvoice;
