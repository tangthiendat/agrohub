import { Table, TableProps } from "antd";
import {
  IExportInvoiceDetailBatch,
  IProductBatch,
  IProductInfo,
} from "../../../interfaces";
import { formatDate } from "../../../utils/datetime";

interface ExportInvoiceDetailBatchTableProps {
  product: IProductInfo;
  detailBatches: IExportInvoiceDetailBatch[];
}

const ExportInvoiceDetailBatchTable: React.FC<
  ExportInvoiceDetailBatchTableProps
> = ({ product, detailBatches }) => {
  const columns: TableProps<IExportInvoiceDetailBatch>["columns"] = [
    {
      title: "Mã lô",
      dataIndex: "productBatch",
      key: "batchId",
      width: "10%",
      render: (productBatch: IProductBatch) => productBatch.batchId,
    },
    {
      title: "Ngày sản xuất",
      key: "manufacturingDate",
      width: "10%",
      render: (_, record: IExportInvoiceDetailBatch) =>
        formatDate(record.productBatch.manufacturingDate),
    },
    {
      title: "Đơn vị tính",
      key: "unit",
      width: "10%",
      render: () => {
        const defaultProductUnit = product.productUnits.find(
          (unit) => unit.isDefault,
        );
        return defaultProductUnit?.unit.unitName;
      },
    },
    {
      title: "Số lượng",
      key: "quantity",
      width: "10%",
      render: (_, record) => record.quantity,
    },
  ];
  return (
    <Table
      rowKey={(record: IExportInvoiceDetailBatch) =>
        record.productBatch.batchId
      }
      className="mt-2"
      columns={columns}
      pagination={{
        pageSize: 5,
        showTotal: (total) => `Tổng ${total} lô`,
      }}
      dataSource={detailBatches}
      size="small"
    />
  );
};

export default ExportInvoiceDetailBatchTable;
