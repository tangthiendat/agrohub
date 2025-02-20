import { Table, TableProps, Tag } from "antd";
import { FilterFilled } from "@ant-design/icons";
import { useQuery } from "@tanstack/react-query";
import { supplierProductService } from "../../services";
import { ISupplier } from "../../interfaces";
import { getFilterIconColor } from "../../utils/color";

interface ProductSupplierTableProps {
  productId: string;
}

const ProductSupplierTable: React.FC<ProductSupplierTableProps> = ({
  productId,
}) => {
  const { data, isLoading } = useQuery({
    queryKey: ["suppliers", productId],
    queryFn: () => supplierProductService.getSupplierByProductId(productId),
  });

  const columns: TableProps<ISupplier>["columns"] = [
    {
      title: "ID",
      dataIndex: "supplierId",
      key: "supplierId",
      width: "10%",
    },
    {
      title: "Tên nhà cung cấp",
      dataIndex: "supplierName",
      key: "supplierName",
      width: "30%",
    },
    {
      title: "Trạng thái",
      dataIndex: "active",
      key: "active",
      width: "15%",
      render: (active: boolean) => (
        <Tag color={active ? "green" : "red"}>
          {active ? "Đã kích hoạt" : "Chưa kích hoạt"}
        </Tag>
      ),
      filters: [
        {
          text: "Đã kích hoạt",
          value: true,
        },
        {
          text: "Chưa kích hoạt",
          value: false,
        },
      ],
      onFilter: (value, record) => record.active === value,
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      width: "25%",
    },
    {
      title: "Số điện thoại",
      dataIndex: "phoneNumber",
      key: "phoneNumber",
      width: "20%",
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: ISupplier) => record.supplierId}
      pagination={{
        showSizeChanger: true,
        showTotal: (total) => `Tổng ${total} nhà cung cấp`,
      }}
      dataSource={data?.payload || []}
      rowClassName={(_, index) =>
        index % 2 === 0 ? "table-row-light" : "table-row-gray"
      }
      rowHoverable={false}
      loading={{
        spinning: isLoading,
        tip: "Đang tải dữ liệu...",
      }}
      size="middle"
    />
  );
};

export default ProductSupplierTable;
