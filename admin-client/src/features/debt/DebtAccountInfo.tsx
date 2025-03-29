import { Descriptions, DescriptionsProps, Table, Tag, Typography } from "antd";
import { TableProps } from "antd/lib";
import { IDebtTransaction, IPartyDebtAccount } from "../../interfaces";
import { formatCurrency } from "../../utils/number";
import { formatDate, formatTimestamp } from "../../utils/datetime";
import { DEBT_STATUS_COLOR, DEBT_STATUS_NAME } from "../../common/constants";
import { getTransactionAmount } from "../../utils/data";
import { DEBT_TRANSACTION_TYPE_NAME } from "../../common/constants/debt";
import { DebtTransactionType } from "../../common/enums";

interface DebtAccountInfoProps {
  debtAccount: IPartyDebtAccount;
}

const DebtAccountInfo: React.FC<DebtAccountInfoProps> = ({ debtAccount }) => {
  const items: DescriptionsProps["items"] = [
    {
      label: "Mã phiếu nhập",
      key: "sourceId",
      children: <span className="font-semibold">{debtAccount.sourceId}</span>,
    },
    {
      label: "Số tiền",
      key: "totalAmount",

      children: formatCurrency(debtAccount.totalAmount),
    },
    {
      label: "Đã trả",
      key: "paidAmount",
      children: formatCurrency(debtAccount.paidAmount),
    },
    {
      label: "Còn lại",
      key: "remainingAmount",
      children: formatCurrency(debtAccount.remainingAmount),
    },
    {
      label: "Ngày đến hạn",
      key: "dueDate",
      children: formatDate(debtAccount.dueDate),
    },
    {
      label: "Lãi suất",
      key: "interestRate",
      children: `${debtAccount.interestRate}%`,
    },
    {
      label: "Trạng thái",
      key: "debtStatus",
      children: (
        <Tag color={DEBT_STATUS_COLOR[debtAccount.debtStatus]}>
          {DEBT_STATUS_NAME[debtAccount.debtStatus]}
        </Tag>
      ),
    },
  ];

  const columns: TableProps<IDebtTransaction>["columns"] = [
    {
      title: "Mã giao dịch",
      dataIndex: "debtTransactionId",
      key: "debtTransactionId",
      width: "15%",
    },
    {
      title: "Số tiền",
      dataIndex: "amount",
      key: "amount",
      width: "15%",
      align: "right",
      render: (value) => (
        <span
          style={{
            color: value > 0 ? "#3f8600" : "#cf1322",
          }}
        >
          {getTransactionAmount(formatCurrency(value))}
        </span>
      ),
    },
    {
      title: "Loại giao dịch",
      dataIndex: "transactionType",
      key: "transactionType",
      width: "15%",
      render: (transactionType: DebtTransactionType) =>
        DEBT_TRANSACTION_TYPE_NAME[transactionType],
    },
    {
      title: "Mã phiếu",
      dataIndex: "sourceId",
      key: "sourceId",
      width: "15%",
    },
    {
      title: "Ngày giao dịch",
      dataIndex: "createdAt",
      key: "createdAt",
      width: "15%",
      render: (createdAt: string) => formatTimestamp(createdAt),
    },
  ];

  return (
    <>
      <Descriptions title="Thông tin chung" items={items} />
      <Typography.Title level={5} className="my-3">
        Giao dịch chi tiết
      </Typography.Title>
      <Table
        columns={columns}
        size="small"
        className="no-border-summary"
        dataSource={debtAccount.debtTransactions}
        rowKey="debtTransactionId"
        pagination={false}
      />
    </>
  );
};

export default DebtAccountInfo;
