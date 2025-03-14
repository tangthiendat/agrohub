import { InputNumber, Select, Table, TableProps } from "antd";
import { useShallow } from "zustand/react/shallow";
import { IProductUnit } from "../../interfaces";
import {
  PurchaseOrderDetailState,
  usePurchaseOrderStore,
} from "../../store/purchase-order-store";
import DeleteIcon from "../../common/components/icons/DeleteIcon";

const PurchaseOrderDetailsTable: React.FC = () => {
  const {
    purchaseOrderDetails,
    updateProductUnit,
    updateQuantity,
    deleteDetail,
  } = usePurchaseOrderStore(
    useShallow((state) => ({
      purchaseOrderDetails: state.purchaseOrderDetails,
      updateProductUnit: state.updateProductUnit,
      updateQuantity: state.updateQuantity,
      deleteDetail: state.deleteDetail,
    })),
  );

  const columns: TableProps<PurchaseOrderDetailState>["columns"] = [
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
      width: "30%",
      render: (product) => product.productName,
    },
    {
      title: "Đơn vị tính",
      dataIndex: "productUnit",
      key: "unit",
      width: "15%",
      render: (productUnit: IProductUnit, record: PurchaseOrderDetailState) => (
        <Select
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
      dataIndex: "quantity",
      key: "quantity",
      width: "15%",
      render: (quantity: number, record: PurchaseOrderDetailState) => (
        <InputNumber
          value={quantity}
          min={1}
          onChange={(value) => {
            updateQuantity(record.product.productId, value as number);
          }}
        />
      ),
    },
    // {
    //   title: "Đơn giá",
    //   dataIndex: "productUnit",
    //   key: "unitPrice",
    //   width: "15%",
    //   render: (productUnit: IProductUnit, record: PurchaseOrderDetailState) => {
    //     const currentProductUnitPrice = getCurrentProductUnitPrice(
    //       record.product,
    //       productUnit.productUnitId,
    //     );
    //     return formatCurrency(currentProductUnitPrice.price);
    //   },
    // },
    // {
    //   title: "Thành tiền",
    //   dataIndex: "productUnit",
    //   key: "total",
    //   width: "15%",
    //   render: (productUnit: IProductUnit, record: PurchaseOrderDetailState) => {
    //     const currentProductUnitPrice = getCurrentProductUnitPrice(
    //       record.product,
    //       productUnit.productUnitId,
    //     );
    //     return formatCurrency(currentProductUnitPrice.price * record.quantity);
    //   },
    // },
    {
      title: "Hành động",
      key: "action",
      width: "10%",
      align: "center",
      render: (_, record: PurchaseOrderDetailState) => (
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
      rowKey={(detail: PurchaseOrderDetailState) => detail.product.productId}
      dataSource={purchaseOrderDetails}
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

export default PurchaseOrderDetailsTable;
