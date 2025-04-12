import {
  Descriptions,
  DescriptionsProps,
  Image,
  Table,
  Typography,
} from "antd";
import { TableProps } from "antd/lib";
import {
  IExportInvoice,
  IExportInvoiceDetail,
  IImportInvoiceDetail,
  IProduct,
} from "../../../interfaces";
import { formatDate } from "../../../utils/datetime";
import { formatCurrency } from "../../../utils/number";
import { getDiscountValue, getVATValue } from "../../../utils/data";
import ExportInvoiceDetailBatchTable from "./ExportInvoiceDetailBatchTable";

interface ExportInvoiceInfoProps {
  exportInvoice: IExportInvoice;
}

const SUMMARY_COLSPAN = 7;

const ExportInvoiceInfo: React.FC<ExportInvoiceInfoProps> = ({
  exportInvoice,
}) => {
  const items: DescriptionsProps["items"] = [
    {
      label: "Mã phiếu xuất",
      key: "exportInvoiceId",
      span: {
        xl: 1,
        lg: 2,
        md: 3,
        sm: 3,
      },
      children: (
        <span className="font-semibold">{exportInvoice.exportInvoiceId}</span>
      ),
    },
    {
      label: "Khách hàng",
      key: "customer",
      span: {
        xl: 1,
        lg: 2,
        md: 3,
        sm: 3,
      },
      children: exportInvoice.customer.customerName,
    },
    {
      label: "Kho",
      key: "warehouse",
      span: {
        xl: 1,
        lg: 2,
        md: 3,
        sm: 3,
      },
      children: exportInvoice.warehouse.warehouseName,
    },
    {
      label: "Ngày lập phiếu",
      key: "createdDate",
      span: {
        xl: 1,
        lg: 2,
        md: 3,
        sm: 3,
      },
      children: formatDate(exportInvoice.createdDate),
    },
    {
      label: "Người lập phiếu",
      key: "user",
      span: {
        xl: 1,
        lg: 2,
        md: 3,
        sm: 3,
      },
      children: exportInvoice.user.fullName,
    },
    ...(exportInvoice?.note
      ? [
          {
            label: "Ghi chú",
            key: "note",
            span: {
              xl: 1,
              lg: 2,
              md: 3,
              sm: 3,
            },
            children: exportInvoice.note,
          },
        ]
      : []),
  ];

  const columns: TableProps<IExportInvoiceDetail>["columns"] = [
    {
      title: "STT",
      width: "5%",
      key: "index",
      render: (_, __, index: number) => index + 1,
    },
    {
      title: "Hình ảnh",
      dataIndex: "product",
      key: "imageUrl",
      width: "10%",
      render: (product: IProduct) => (
        <Image src={product?.imageUrl} width={75} height={75} />
      ),
    },
    {
      title: "Tên sản phẩm",
      dataIndex: "product",
      key: "product",
      width: "25%",
      render: (product) => product.productName,
    },
    {
      title: "Đơn vị tính",
      dataIndex: "productUnit",
      key: "productUnit",
      width: "15%",
      render: (productUnit: IImportInvoiceDetail["productUnit"]) =>
        productUnit.unit.unitName,
    },
    {
      title: "Số lượng",
      dataIndex: "quantity",
      key: "quantity",
      align: "right",
      width: "10%",
    },
    {
      title: "Đơn giá",
      dataIndex: "unitPrice",
      key: "unitPrice",
      width: "15%",
      align: "right",
      render: (price) => formatCurrency(price),
    },
    {
      title: "Thành tiền",
      dataIndex: "amount",
      key: "amount",
      width: "15%",
      align: "right",
      render: (_, record: IExportInvoiceDetail) =>
        formatCurrency(record.unitPrice * record.quantity),
    },
  ];

  return (
    <>
      <Descriptions title="Thông tin chung" items={items} />
      <Typography.Title level={5} className="my-3">
        Chi tiết phiếu xuất
      </Typography.Title>
      <Table
        columns={columns}
        size="small"
        className="no-border-summary"
        dataSource={exportInvoice.exportInvoiceDetails}
        rowKey="importInvoiceDetailId"
        expandable={{
          expandedRowRender: (record: IExportInvoiceDetail) => (
            <ExportInvoiceDetailBatchTable
              product={record.product}
              detailBatches={record.detailBatches}
            />
          ),
          rowExpandable: (record: IExportInvoiceDetail) =>
            record.detailBatches.length > 0,
        }}
        pagination={false}
        summary={() => {
          return (
            <>
              <Table.Summary.Row>
                <Table.Summary.Cell
                  index={0}
                  colSpan={SUMMARY_COLSPAN}
                  align="right"
                  className="!pt-4 font-semibold"
                >
                  Tổng cộng:
                </Table.Summary.Cell>
                <Table.Summary.Cell index={1} align="right" className="!pt-4">
                  {formatCurrency(exportInvoice.totalAmount)}
                </Table.Summary.Cell>
              </Table.Summary.Row>
              <Table.Summary.Row>
                <Table.Summary.Cell
                  index={0}
                  colSpan={SUMMARY_COLSPAN}
                  align="right"
                  className="font-semibold"
                >
                  VAT:
                </Table.Summary.Cell>
                <Table.Summary.Cell index={1} align="right">
                  {formatCurrency(
                    getVATValue(
                      exportInvoice.totalAmount,
                      exportInvoice.vatRate,
                    ),
                  )}
                </Table.Summary.Cell>
              </Table.Summary.Row>
              <Table.Summary.Row>
                <Table.Summary.Cell
                  index={0}
                  colSpan={SUMMARY_COLSPAN}
                  align="right"
                  className="font-semibold"
                >
                  Giảm giá:
                </Table.Summary.Cell>
                <Table.Summary.Cell index={1} align="right">
                  {getDiscountValue(
                    exportInvoice.totalAmount,
                    exportInvoice.discountValue,
                    exportInvoice.discountType,
                  ) > 0
                    ? `-${formatCurrency(
                        getDiscountValue(
                          exportInvoice.totalAmount,
                          exportInvoice.discountValue,
                          exportInvoice.discountType,
                        ),
                      )}`
                    : 0}
                </Table.Summary.Cell>
              </Table.Summary.Row>

              <Table.Summary.Row>
                <Table.Summary.Cell
                  index={0}
                  colSpan={SUMMARY_COLSPAN}
                  align="right"
                  className="font-semibold"
                >
                  Thành tiền:
                </Table.Summary.Cell>
                <Table.Summary.Cell index={1} align="right">
                  {formatCurrency(exportInvoice.finalAmount)}
                </Table.Summary.Cell>
              </Table.Summary.Row>
            </>
          );
        }}
      />
    </>
  );
};

export default ExportInvoiceInfo;
