import { Descriptions, DescriptionsProps, Table, Typography } from "antd";
import { IImportInvoice, IImportInvoiceDetail } from "../../interfaces";
import { formatDate } from "../../utils/datetime";
import { TableProps } from "antd/lib";
import { formatCurrency } from "../../utils/number";
import { getDiscountValue, getVATValue } from "../../utils/data";

interface ImportInvoiceInfoProps {
  importInvoice: IImportInvoice;
}

const ImportInvoiceInfo: React.FC<ImportInvoiceInfoProps> = ({
  importInvoice,
}) => {
  const items: DescriptionsProps["items"] = [
    {
      label: "Mã phiếu nhập",
      key: "importInvoiceId",
      span: {
        xl: 1,
        lg: 2,
        md: 3,
        sm: 3,
      },
      children: (
        <span className="font-semibold">{importInvoice.importInvoiceId}</span>
      ),
    },
    {
      label: "Nhà cung cấp",
      key: "supplier",
      span: {
        xl: 1,
        lg: 2,
        md: 3,
        sm: 3,
      },
      children: importInvoice.supplier.supplierName,
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
      children: importInvoice.warehouse.warehouseName,
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
      children: formatDate(importInvoice.createdDate),
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
      children: importInvoice.user.fullName,
    },
    ...(importInvoice?.note
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
            children: importInvoice.note,
          },
        ]
      : []),
  ];

  const columns: TableProps<IImportInvoiceDetail>["columns"] = [
    {
      title: "Tên sản phẩm",
      dataIndex: "product",
      key: "product",
      width: "20%",
      render: (product) => product.productName,
    },
    {
      title: "Đơn vị tính",
      dataIndex: "productUnit",
      key: "productUnit",
      width: "10%",
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
      width: "10%",
      align: "right",
      render: (price) => formatCurrency(price),
    },
    {
      title: "Thành tiền",
      dataIndex: "amount",
      key: "amount",
      width: "10%",
      align: "right",
      render: (_, record: IImportInvoiceDetail) =>
        formatCurrency(record.unitPrice * record.quantity),
    },
  ];

  return (
    <>
      <Descriptions title="Thông tin chung" items={items} />
      <Typography.Title level={5} className="my-3">
        Chi tiết phiếu nhập
      </Typography.Title>
      <Table
        columns={columns}
        size="small"
        className="no-border-summary"
        dataSource={importInvoice.importInvoiceDetails}
        rowKey="importInvoiceDetailId"
        pagination={false}
        summary={() => {
          return (
            <>
              <Table.Summary.Row>
                <Table.Summary.Cell
                  index={0}
                  colSpan={4}
                  align="right"
                  className="!pt-4 font-semibold"
                >
                  Tổng cộng:
                </Table.Summary.Cell>
                <Table.Summary.Cell index={1} align="right" className="!pt-4">
                  {formatCurrency(importInvoice.totalAmount)}
                </Table.Summary.Cell>
              </Table.Summary.Row>
              <Table.Summary.Row>
                <Table.Summary.Cell
                  index={0}
                  colSpan={4}
                  align="right"
                  className="font-semibold"
                >
                  VAT:
                </Table.Summary.Cell>
                <Table.Summary.Cell index={1} align="right">
                  {formatCurrency(
                    getVATValue(
                      importInvoice.totalAmount,
                      importInvoice.vatRate,
                    ),
                  )}
                </Table.Summary.Cell>
              </Table.Summary.Row>
              <Table.Summary.Row>
                <Table.Summary.Cell
                  index={0}
                  colSpan={4}
                  align="right"
                  className="font-semibold"
                >
                  Giảm giá:
                </Table.Summary.Cell>
                <Table.Summary.Cell index={1} align="right">
                  {getDiscountValue(
                    importInvoice.totalAmount,
                    importInvoice.discountValue,
                    importInvoice.discountType,
                  ) > 0
                    ? `-${formatCurrency(
                        getDiscountValue(
                          importInvoice.totalAmount,
                          importInvoice.discountValue,
                          importInvoice.discountType,
                        ),
                      )}`
                    : 0}
                </Table.Summary.Cell>
              </Table.Summary.Row>

              <Table.Summary.Row>
                <Table.Summary.Cell
                  index={0}
                  colSpan={4}
                  align="right"
                  className="font-semibold"
                >
                  Thành tiền:
                </Table.Summary.Cell>
                <Table.Summary.Cell index={1} align="right">
                  {formatCurrency(importInvoice.finalAmount)}
                </Table.Summary.Cell>
              </Table.Summary.Row>
            </>
          );
        }}
      />
    </>
  );
};

export default ImportInvoiceInfo;
