import { useState } from "react";
import { Modal } from "antd";
import ViewIcon from "../../../common/components/icons/ViewIcon";
import { IExportInvoice } from "../../../interfaces";
import ExportInvoiceInfo from "./ExportInvoiceInfo";

interface ViewExportInvoiceProps {
  exportInvoice: IExportInvoice;
}

const ViewExportInvoice: React.FC<ViewExportInvoiceProps> = ({
  exportInvoice,
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
        <ExportInvoiceInfo exportInvoice={exportInvoice} />
      </Modal>
    </>
  );
};

export default ViewExportInvoice;
