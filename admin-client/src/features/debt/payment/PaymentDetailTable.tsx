import { InputNumber, Table, TableProps } from "antd";
import { useShallow } from "zustand/react/shallow";
import {
  PaymentDetailState,
  usePaymentStore,
} from "../../../store/payment-store";
import { formatDate } from "../../../utils/datetime";
import { formatCurrency, parseCurrency } from "../../../utils/number";

const PaymentDetailTable: React.FC = () => {
  const { paymentDetails } = usePaymentStore(
    useShallow((state) => ({
      paymentDetails: state.paymentDetails,
    })),
  );

  const columns: TableProps<PaymentDetailState>["columns"] = [
    {
      title: "STT",
      width: "5%",
      key: "index",
      render: (_, __, index: number) => index + 1,
    },
    {
      title: "Mã phiếu",
      key: "importInvoiceId",
      width: "10%",
      render: (_, record: PaymentDetailState) => record.debtAccount.sourceId,
    },
    {
      title: "Số tiền nợ",
      key: "debtAmount",
      width: "15%",
      align: "right",
      render: (_, record: PaymentDetailState) =>
        formatCurrency(record.debtAccount.totalAmount),
    },
    {
      title: "Đã trả",
      key: "paidAmount",
      width: "15%",
      align: "right",
      render: (_, record: PaymentDetailState) =>
        formatCurrency(record.debtAccount.paidAmount),
    },
    {
      title: "Còn lại",
      key: "remainingAmount",
      width: "15%",
      align: "right",
      render: (_, record: PaymentDetailState) =>
        formatCurrency(record.debtAccount.remainingAmount),
    },
    {
      title: "Ngày đến hạn",
      key: "dueDate",
      width: "15%",
      render: (_, record: PaymentDetailState) =>
        formatDate(record.debtAccount.dueDate),
    },
    {
      title: "Số tiền trả",
      key: "paymentAmount",
      width: "15%",
      render: (_, record: PaymentDetailState) => (
        <InputNumber
          className="right-aligned-number w-full"
          controls={false}
          readOnly
          value={record.paymentAmount}
          min={0}
          step={1000}
          formatter={(value) => formatCurrency(value)}
          parser={(value) => parseCurrency(value) as unknown as 0}
          // onChange={(value) => {
          //   updatePaymentDetail(record.debtAccount.sourceId, value as number);
          // }}
          // addonAfter="VND"
        />
      ),
    },
  ];

  return (
    <Table
      className="mb-4"
      rowKey="debtAccountId"
      dataSource={paymentDetails}
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

export default PaymentDetailTable;
