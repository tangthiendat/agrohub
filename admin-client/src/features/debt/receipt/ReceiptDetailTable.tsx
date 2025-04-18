import { InputNumber, Table, TableProps } from "antd";
import { formatDate } from "../../../utils/datetime";
import { formatCurrency, parseCurrency } from "../../../utils/number";
import { IPartyDebtAccount } from "../../../interfaces";

interface ReceiptDetailTableProps {
  receiptDetails: IPartyDebtAccount[];
}

const ReceiptDetailTable: React.FC<ReceiptDetailTableProps> = ({
  receiptDetails,
}) => {
  // const { receiptDetails } = useReceiptStore(
  //   useShallow((state) => ({
  //     receiptDetails: state.receiptDetails,
  //   })),
  // );

  const columns: TableProps<IPartyDebtAccount>["columns"] = [
    {
      title: "STT",
      width: "5%",
      key: "index",
      render: (_, __, index: number) => index + 1,
    },
    {
      title: "Mã phiếu",
      key: "exportInvoiceId",
      width: "10%",
      render: (_, record: IPartyDebtAccount) => record.sourceId,
    },
    {
      title: "Số tiền nợ",
      key: "debtAmount",
      width: "15%",
      align: "right",
      render: (_, record: IPartyDebtAccount) =>
        formatCurrency(record.totalAmount),
    },
    {
      title: "Đã trả",
      key: "paidAmount",
      width: "15%",
      align: "right",
      render: (_, record: IPartyDebtAccount) =>
        formatCurrency(record.paidAmount),
    },
    {
      title: "Còn lại",
      key: "remainingAmount",
      width: "15%",
      align: "right",
      render: (_, record: IPartyDebtAccount) =>
        formatCurrency(record.remainingAmount),
    },
    {
      title: "Ngày đến hạn",
      key: "dueDate",
      width: "15%",
      render: (_, record: IPartyDebtAccount) => formatDate(record.dueDate),
    },
    {
      title: "Số tiền thu",
      key: "receiptAmount",
      width: "15%",
      render: (_, record: IPartyDebtAccount) => (
        <InputNumber
          className="right-aligned-number w-full"
          controls={false}
          readOnly
          // value={record.receiptAmount}
          min={0}
          step={1000}
          formatter={(value) => formatCurrency(value)}
          parser={(value) => parseCurrency(value) as unknown as 0}
        />
      ),
    },
  ];

  return (
    <Table
      className="mb-4"
      rowKey={(record) => record.debtAccountId}
      dataSource={receiptDetails}
      pagination={false}
      columns={columns}
      bordered={false}
      size="middle"
      rowClassName={(_, index) =>
        index % 2 === 0 ? "table-row-light" : "table-row-gray"
      }
      rowHoverable={false}
    />
  );
};

export default ReceiptDetailTable;
