import { InputNumber, Table, Typography } from "antd";
import { TableProps } from "antd/lib";
import {
  SelectedBatchState,
  SelectedLocationState,
  useExportInvoiceStore,
} from "../../../store/export-invoice-store";
import { getLocationName } from "../../../utils/data";
import { formatDate } from "../../../utils/datetime";

interface BatchesTableProps {
  productId: string;
}

const BatchesTable: React.FC<BatchesTableProps> = ({ productId }) => {
  const exportInvoiceDetails = useExportInvoiceStore(
    (state) => state.exportInvoiceDetails,
  );

  const selectedBatches =
    exportInvoiceDetails.find(
      (detail) => detail.product.productId === productId,
    )?.selectedBatches || [];

  const columns: TableProps<SelectedBatchState>["columns"] = [
    {
      title: "Mã lô",
      key: "batchId",
      width: "10%",
      render: (_, record) => record.productBatch.batchId,
    },
    {
      title: "Thông tin sản xuất",
      key: "dates",
      width: "10%",
      render: (_, record) => (
        <div className="flex flex-col">
          <Typography.Text>
            NSX: {formatDate(record.productBatch.manufacturingDate)}
          </Typography.Text>
          <Typography.Text>
            HSD: {formatDate(record.productBatch.expirationDate)}
          </Typography.Text>
        </div>
      ),
    },
    {
      title: "Số lượng",
      key: "quantity",
      width: "10%",
      render: (_, record) => record.productBatch.quantity,
    },
    {
      title: "Đơn vị tính",
      key: "unit",
      width: "10%",
      render: () => {
        const currentDetailProduct = exportInvoiceDetails.find(
          (detail) => detail.product.productId === productId,
        )?.product;
        const defaultProductUnit = currentDetailProduct?.productUnits.find(
          (unit) => unit.isDefault,
        );
        return defaultProductUnit?.unit.unitName;
      },
    },
    {
      title: "Vị trí",
      key: "locations",
      width: "35%",
      render: (_, record) => {
        return (
          <table className="w-full border-collapse">
            <thead>
              <tr className="border-b">
                <th className="text-left text-xs font-medium text-gray-500">
                  Vị trí
                </th>
                <th className="text-left text-xs font-medium text-gray-500">
                  Có sẵn
                </th>
                <th className="text-left text-xs font-medium text-gray-500">
                  Số lượng xuất
                </th>
              </tr>
            </thead>
            <tbody>
              {record.selectedLocations?.map(
                (selectedLocation: SelectedLocationState) => (
                  <tr
                    key={selectedLocation.location.batchLocationId}
                    className="border-b"
                  >
                    <td className="py-1 text-sm text-gray-700">
                      {getLocationName(
                        selectedLocation.location.productLocation,
                      )}
                    </td>
                    <td className="py-1 text-sm text-gray-700">
                      {selectedLocation.location.quantity}
                    </td>
                    <td className="py-1">
                      <InputNumber
                        key={selectedLocation.quantity}
                        readOnly
                        className="w-[100px]"
                        min={0}
                        max={selectedLocation.location.quantity}
                        defaultValue={selectedLocation.quantity}
                        size="small"
                      />
                    </td>
                  </tr>
                ),
              )}
            </tbody>
          </table>
        );
      },
    },
    {
      title: "Tổng số lượng xuất",
      key: "totalQuantity",
      width: "15%",
      render: (_, record) => record.quantity,
    },
  ];

  return (
    <Table
      rowKey={(record: SelectedBatchState) => record.productBatch.batchId}
      className="mt-2"
      columns={columns}
      pagination={{
        pageSize: 5,
        showTotal: (total) => `Tổng ${total} lô`,
      }}
      dataSource={selectedBatches}
      size="small"
    />
  );
};

export default BatchesTable;
