import {
  CaretDownFilled,
  CaretUpFilled,
  CheckCircleFilled,
  CloseCircleFilled,
} from "@ant-design/icons";
import { Space, Table, TablePaginationConfig } from "antd";
import { TableProps } from "antd/lib";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import AddBatchLocation from "./AddBatchLocation";
import ViewBatchLocation from "./ViewBatchLocation";
import { IProductBatch, IProductInfo, Page } from "../../../interfaces";
import { getSortDownIconColor, getSortUpIconColor } from "../../../utils/color";
import { formatDate } from "../../../utils/datetime";
import { getDefaultSortOrder, getSortDirection } from "../../../utils/filter";

interface ProductBatchTableProps {
  productBatchPage?: Page<IProductBatch>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const ProductBatchTable: React.FC<ProductBatchTableProps> = ({
  productBatchPage,
  isLoading,
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} lô hàng`,
    },
  }));

  useEffect(() => {
    if (productBatchPage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: productBatchPage.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} lô hàng`,
        },
      }));
    }
  }, [productBatchPage]);

  const handleTableChange: TableProps<IProductBatch>["onChange"] = (
    pagination,
    filters,
    sorter,
  ) => {
    setTableParams((prev) => ({
      ...prev,
      pagination,
      sorter,
      filters,
    }));

    searchParams.set("page", String(pagination.current));
    searchParams.set("pageSize", String(pagination.pageSize));

    if (filters) {
      Object.entries(filters).forEach(([key, value]) => {
        if (!value) {
          searchParams.delete(key);
          return;
        }
        const paramValue = Array.isArray(value)
          ? value.join(",")
          : String(value);
        searchParams.set(key, paramValue);
      });
    }

    let sortBy;
    let direction;

    if (sorter) {
      if (Array.isArray(sorter)) {
        sortBy = sorter[0].field as string;
        direction = getSortDirection(sorter[0].order as string);
      } else {
        sortBy = sorter.field as string;
        direction = getSortDirection(sorter.order as string);
      }
    }

    if (sortBy && direction) {
      searchParams.set("sortBy", sortBy);
      searchParams.set("direction", direction);
    } else {
      searchParams.delete("direction");
      searchParams.delete("sortBy");
    }

    setSearchParams(searchParams);
  };

  const columns: TableProps<IProductBatch>["columns"] = [
    {
      title: "Mã lô hàng",
      dataIndex: "batchId",
      key: "batchId",
      width: "10%",
    },
    {
      title: "Sản phẩm",
      dataIndex: "product",
      key: "productName",
      width: "20%",
      render: (product: IProductInfo) => product.productName,
    },
    {
      title: "Ngày sản xuất",
      dataIndex: "manufacturingDate",
      key: "manufacturingDate",
      width: "10%",
      render: (manufacturingDate: string) => formatDate(manufacturingDate),
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "manufacturingDate"),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Hạn sử dụng",
      dataIndex: "expirationDate",
      key: "expirationDate",
      width: "10%",
      render: (expirationDate: string) => formatDate(expirationDate),
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "expirationDate"),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Số lượng",
      dataIndex: "quantity",
      key: "quantity",
      width: "10%",
      align: "right",
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "quantity"),
      sortIcon: ({ sortOrder }) => (
        <div className="ml-1 flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Đơn vị tính",
      key: "unit",
      width: "10%",
      render: (_, record: IProductBatch) => {
        const productUnit = record.product.productUnits.find(
          (unit) => unit.isDefault,
        );
        return productUnit?.unit.unitName;
      },
    },
    {
      title: "Đã sắp xếp",
      key: "isArranged",
      width: "10%",
      align: "center",
      render: (_, record: IProductBatch) => {
        const isArranged =
          record.batchLocations && record.batchLocations.length > 0;
        return isArranged ? (
          <CheckCircleFilled className="text-lg text-[#4CAF50]" />
        ) : (
          <CloseCircleFilled className="text-lg text-[#F44336]" />
        );
      },
    },
    {
      title: "Hành động",
      key: "action",
      width: "15%",
      render: (_, record: IProductBatch) => {
        return (
          <Space>
            <ViewBatchLocation productBatch={record} />
            <AddBatchLocation productBatch={record} />
          </Space>
        );
      },
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: IProductBatch) => record.batchId}
      pagination={tableParams.pagination}
      dataSource={productBatchPage?.content}
      rowClassName={(_, index) =>
        index % 2 === 0 ? "table-row-light" : "table-row-gray"
      }
      rowHoverable={false}
      loading={{
        spinning: isLoading,
        tip: "Đang tải dữ liệu...",
      }}
      onChange={handleTableChange}
      size="middle"
    />
  );
};

export default ProductBatchTable;
