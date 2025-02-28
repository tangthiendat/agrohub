import { useEffect, useState } from "react";
import { FormInstance, Table, TableProps } from "antd";
import { IProduct, IProductUnit, IProductUnitPrice } from "../../interfaces";
import { formatDate } from "../../utils/datetime";
import { formatCurrency } from "../../utils/number";
import dayjs from "dayjs";

interface ProductUnitPriceTableProps {
  productForm: FormInstance<IProduct>;
  productUnitIndex: number;
}

const ProductUnitPriceTable: React.FC<ProductUnitPriceTableProps> = ({
  productForm,
  productUnitIndex,
}) => {
  const [productUnitPrices, setProductUnitPrices] = useState<
    IProductUnitPrice[]
  >([]);

  useEffect(() => {
    const productUnit: IProductUnit = productForm.getFieldValue([
      "productUnits",
      productUnitIndex,
    ]);
    const sortedProductUnitPrices = productUnit?.productUnitPrices?.sort(
      (a, b) => dayjs(b.validTo).diff(dayjs(a.validTo)),
    );
    setProductUnitPrices(sortedProductUnitPrices);
  }, [productForm, productUnitIndex]);

  const columns: TableProps<IProductUnitPrice>["columns"] = [
    {
      title: "Giá",
      dataIndex: "price",
      key: "price",
      render: (price) => `${formatCurrency(price)} VND`,
    },
    {
      title: "Từ ngày",
      dataIndex: "validFrom",
      key: "validFrom",
      render: (validFrom: string) => (validFrom ? formatDate(validFrom) : ""),
    },
    {
      title: "Đến ngày",
      dataIndex: "validTo",
      key: "validTo",
      render: (validTo: string) => (validTo ? formatDate(validTo) : ""),
    },
  ];

  return (
    <Table
      size="small"
      bordered={false}
      rowKey={(record) => record.productUnitPriceId || crypto.randomUUID()}
      columns={columns}
      dataSource={productUnitPrices}
      pagination={{
        showSizeChanger: true,
        showTotal: (total) => `Tổng ${total} giá`,
      }}
      rowClassName={(_, index) =>
        index % 2 === 0 ? "table-row-light" : "table-row-gray"
      }
    />
  );
};

export default ProductUnitPriceTable;
