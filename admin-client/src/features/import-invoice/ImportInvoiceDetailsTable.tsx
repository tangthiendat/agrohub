import { InputNumber, Select, Table, TableProps, Typography } from "antd";
import { FormInstance } from "antd/lib";
import { useShallow } from "zustand/react/shallow";
import DeleteIcon from "../../common/components/icons/DeleteIcon";
import NewBatchForm from "./NewBatchForm";
import { IProductUnit } from "../../interfaces";
import {
  ImportInvoiceDetailState,
  useImportInvoiceStore,
} from "../../store/import-invoice-store";
import { formatCurrency, parseCurrency } from "../../utils/number";

interface ImportInvoiceDetailsTableProps {
  form: FormInstance;
}

const ImportInvoiceDetailsTable: React.FC<ImportInvoiceDetailsTableProps> = ({
  form,
}) => {
  const {
    totalAmount,
    finalAmount,
    importInvoiceDetails,
    updateProductUnit,
    updateQuantity,
    updateUnitPrice,
    deleteDetail,
  } = useImportInvoiceStore(
    useShallow((state) => ({
      totalAmount: state.totalAmount,
      finalAmount: state.finalAmount,
      importInvoiceDetails: state.importInvoiceDetails,
      updateProductUnit: state.updateProductUnit,
      updateQuantity: state.updateQuantity,
      updateUnitPrice: state.updateUnitPrice,
      deleteDetail: state.deleteDetail,
    })),
  );

  const columns: TableProps<ImportInvoiceDetailState>["columns"] = [
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
      render: (productUnit: IProductUnit, record: ImportInvoiceDetailState) => (
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
      render: (_, record: ImportInvoiceDetailState) => (
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
      render: (unitPrice: number, record: ImportInvoiceDetailState) => (
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
          addonAfter="VND"
        />
      ),
    },
    {
      title: "Thành tiền",
      key: "total",
      width: "15%",
      render: (_, record: ImportInvoiceDetailState) => (
        <InputNumber
          value={(record.unitPrice || 0) * record.quantity}
          className="right-aligned-number w-full"
          controls={false}
          readOnly
          formatter={(value) => formatCurrency(value)}
          parser={(value) => parseCurrency(value) as unknown as 0}
          step={1000}
          min={0}
          addonAfter="VND"
        />
      ),
    },
    {
      title: "Hành động",
      key: "action",
      width: "10%",
      align: "center",
      render: (_, record: ImportInvoiceDetailState) => (
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
      rowKey={(detail: ImportInvoiceDetailState) => detail.product.productId}
      dataSource={importInvoiceDetails}
      pagination={false}
      expandable={{
        expandedRowRender: (record: ImportInvoiceDetailState) => (
          <>
            <Typography.Title level={5} className="mb-2">
              Thông tin lô hàng
            </Typography.Title>
            <NewBatchForm importInvoiceDetail={record} />
          </>
        ),
        // expandRowByClick: true,
      }}
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

export default ImportInvoiceDetailsTable;
