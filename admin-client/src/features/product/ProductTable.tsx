import { useEffect, useState } from "react";
import { Space, Table, TablePaginationConfig } from "antd";
import {
  CaretDownFilled,
  CaretUpFilled,
  FilterFilled,
} from "@ant-design/icons";
import { useNavigate, useSearchParams } from "react-router";
import { useQuery } from "@tanstack/react-query";
import { TableProps } from "antd/lib";
import ViewIcon from "../../common/components/icons/ViewIcon";
import { IProduct, Page } from "../../interfaces";
import { categoryService } from "../../services";
import { formatTimestamp } from "../../utils/datetime";
import {
  getDefaultFilterValue,
  getDefaultSortOrder,
  getSortDirection,
} from "../../utils/filter";
import {
  getFilterIconColor,
  getSortDownIconColor,
  getSortUpIconColor,
} from "../../utils/color";

interface ProductTableProps {
  productPage?: Page<IProduct>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const ProductTable: React.FC<ProductTableProps> = ({
  productPage,
  isLoading,
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} sản phẩm`,
    },
  }));

  const { data: categoryOptions, isLoading: isCategoriesLoading } = useQuery({
    queryKey: ["categories"],
    queryFn: categoryService.getAll,
    select: (data) => {
      if (data.payload) {
        return data.payload.map((category) => ({
          value: category.categoryId,
          text: category.categoryName,
        }));
      }
    },
  });

  useEffect(() => {
    if (productPage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: productPage.meta?.totalElements || 0,
        },
      }));
    }
  }, [productPage]);

  const handleTableChange: TableProps<IProduct>["onChange"] = (
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
        const paramKey = key === "category" ? "categoryId" : key;
        if (!value) {
          searchParams.delete(paramKey);
          return;
        }
        const paramValue = Array.isArray(value)
          ? value.join(",")
          : String(value);

        searchParams.set(paramKey, paramValue);
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

  const columns: TableProps<IProduct>["columns"] = [
    {
      title: "Mã sản phẩm",
      dataIndex: "productId",
      key: "productId",
      width: "12%",
    },
    {
      title: "Tên sản phẩm",
      dataIndex: "productName",
      key: "productName",
      width: "20%",
    },
    {
      title: "Tổng số lượng",
      dataIndex: "totalQuantity",
      key: "totalQuantity",
      width: "13%",
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "totalQuantity"),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Danh mục",
      dataIndex: "category",
      key: "category",
      width: "15%",
      render: (category) => category.categoryName,
      filters: categoryOptions,
      defaultFilteredValue: getDefaultFilterValue(searchParams, "categoryId"),
      filterIcon: (filtered) => (
        <FilterFilled style={{ color: getFilterIconColor(filtered) }} />
      ),
    },
    {
      title: "Thời gian tạo",
      dataIndex: "createdAt",
      key: "createdAt",
      width: "15%",
      render: (createdAt: string) =>
        createdAt ? formatTimestamp(createdAt) : "",
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "createdAt"),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Thời gian cập nhật",
      dataIndex: "updatedAt",
      key: "updatedAt",
      width: "15%",
      render: (updatedAt: string) =>
        updatedAt ? formatTimestamp(updatedAt) : "",
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "updatedAt"),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Hành động",
      key: "action",
      width: "10%",
      render: (record: IProduct) => (
        <Space>
          <ViewIcon onClick={() => navigate(record.productId)} />
        </Space>
      ),
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: IProduct) => record.productId}
      pagination={tableParams.pagination}
      dataSource={productPage?.content}
      rowClassName={(_, index) =>
        index % 2 === 0 ? "table-row-light" : "table-row-gray"
      }
      rowHoverable={false}
      loading={{
        spinning: isLoading || isCategoriesLoading,
        tip: "Đang tải dữ liệu...",
      }}
      onChange={handleTableChange}
      size="middle"
    />
  );
};

export default ProductTable;
