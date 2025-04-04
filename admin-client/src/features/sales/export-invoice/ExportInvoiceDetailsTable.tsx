import { FormInstance, InputNumber, Select, Table } from "antd";
import {
  ExportInvoiceDetailState,
  useExportInvoiceStore,
} from "../../../store/export-invoice-store";
import { useShallow } from "zustand/react/shallow";
import { TableProps } from "antd/lib";
import DeleteIcon from "../../../common/components/icons/DeleteIcon";
import { IProductUnit } from "../../../interfaces";
import { formatCurrency, parseCurrency } from "../../../utils/number";

interface ExportInvoiceDetailsTableProps {
  form: FormInstance;
}

const ExportInvoiceDetailsTable: React.FC<ExportInvoiceDetailsTableProps> = ({
  form,
}) => {
  const {
    totalAmount,
    finalAmount,
    exportInvoiceDetails,
    updateProductUnit,
    updateQuantity,
    updateUnitPrice,
    deleteDetail,
  } = useExportInvoiceStore(
    useShallow((state) => ({
      totalAmount: state.totalAmount,
      finalAmount: state.finalAmount,
      exportInvoiceDetails: state.exportInvoiceDetails,
      updateProductUnit: state.updateProductUnit,
      updateQuantity: state.updateQuantity,
      updateUnitPrice: state.updateUnitPrice,
      deleteDetail: state.deleteDetail,
    })),
  );

  const columns: TableProps<ExportInvoiceDetailState>["columns"] = [
    {
      title: "STT",
      width: "5%",
      key: "index",
      render: (_, __, index: number) => index + 1,
    },
    {
      title: "Mã sản phẩm",
      dataIndex: "product",
      key: "productId",
      width: "10%",
      render: (product) => product.productId,
    },
    {
      title: "Tên sản phẩm",
      dataIndex: "product",
      key: "productName",
      width: "25%",
      render: (product) => product.productName,
    },
    {
      title: "Đơn vị tính",
      dataIndex: "productUnit",
      key: "unit",
      width: "10%",
      render: (productUnit: IProductUnit, record: ExportInvoiceDetailState) => (
        <Select
          onClick={(e) => e.stopPropagation()}
          value={productUnit.productUnitId}
          options={record.product.productUnits.map((pu) => ({
            value: pu.productUnitId,
            label: pu.unit.unitName,
          }))}
          onChange={(value) => {
            updateProductUnit(record.product.productId, value);
          }}
        />
      ),
    },
    {
      title: "Số lượng",
      key: "quantity",
      width: "10%",
      render: (_, record: ExportInvoiceDetailState) => (
        <InputNumber
          value={record.quantity}
          min={1}
          onClick={(e) => e.stopPropagation()}
          onChange={(value) => {
            updateQuantity(record.product.productId, value as number);
            form.setFieldsValue({
              totalAmount: totalAmount,
              finalAmount: finalAmount,
            });
          }}
        />
      ),
    },
    {
      title: "Đơn giá",
      dataIndex: "unitPrice",
      key: "unitPrice",
      width: "15%",
      render: (unitPrice: number, record: ExportInvoiceDetailState) => (
        <InputNumber
          className="right-aligned-number w-full"
          controls={false}
          value={unitPrice}
          min={0}
          step={1000}
          formatter={(value) => formatCurrency(value)}
          parser={(value) => parseCurrency(value) as unknown as 0}
          onChange={(value) => {
            updateUnitPrice(record.product.productId, value as number);
            form.setFieldsValue({
              totalAmount: totalAmount,
              finalAmount: finalAmount,
            });
          }}
          // addonAfter="VND"
        />
      ),
    },
    {
      title: "Thành tiền",
      key: "total",
      width: "15%",
      render: (_, record: ExportInvoiceDetailState) => (
        <InputNumber
          value={(record.unitPrice || 0) * record.quantity}
          className="right-aligned-number w-full"
          controls={false}
          readOnly
          formatter={(value) => formatCurrency(value)}
          parser={(value) => parseCurrency(value) as unknown as 0}
          step={1000}
          min={0}
          // addonAfter="VND"
        />
      ),
    },
    {
      title: "Hành động",
      key: "action",
      width: "10%",
      align: "center",
      render: (_, record: ExportInvoiceDetailState) => (
        <DeleteIcon
          tooltipTitle="Xoá"
          onClick={() => deleteDetail(record.product.productId)}
        />
      ),
    },
  ];

  return (
    <Table
      className="mb-4"
      rowKey={(detail: ExportInvoiceDetailState) => detail.product.productId}
      dataSource={exportInvoiceDetails}
      pagination={false}
      // expandable={{
      //   expandedRowRender: (record: ExportInvoiceDetailState) => (
      //     <>
      //       <Typography.Title level={5} className="mb-2">
      //         Thông tin lô hàng
      //       </Typography.Title>
      //       <NewBatchForm importInvoiceDetail={record} />
      //     </>
      //   ),
      //   // expandRowByClick: true,
      // }}
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

export default ExportInvoiceDetailsTable;
