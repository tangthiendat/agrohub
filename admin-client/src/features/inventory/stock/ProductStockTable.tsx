import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import { TableProps } from "antd/lib";
import { Image, Table, TablePaginationConfig } from "antd";
import {
  CaretDownFilled,
  CaretUpFilled,
  FilterFilled,
} from "@ant-design/icons";
import { IProductStock, Page } from "../../../interfaces";
import {
  getDefaultFilterValue,
  getDefaultSortOrder,
  getSortDirection,
} from "../../../utils/filter";
import {
  getFilterIconColor,
  getSortDownIconColor,
  getSortUpIconColor,
} from "../../../utils/color";
import { useQuery } from "@tanstack/react-query";
import { categoryService } from "../../../services";

interface ProductStockTableProps {
  productStockPage?: Page<IProductStock>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const ProductStockTable: React.FC<ProductStockTableProps> = ({
  productStockPage,
  isLoading,
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} hàng tồn kho`,
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
    if (productStockPage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: productStockPage.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} hàng tồn kho`,
        },
      }));
    }
  }, [productStockPage]);

  const handleTableChange: TableProps<IProductStock>["onChange"] = (
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

  const columns: TableProps<IProductStock>["columns"] = [
    {
      title: "Mã sản phẩm",
      key: "productId",
      width: "10%",
      render: (_, productStock: IProductStock) =>
        productStock.product.productId,
    },
    {
      title: "Hình ảnh",
      key: "image",
      width: "10%",
      render: (_, productStock: IProductStock) => (
        <Image src={productStock.product.imageUrl} width={75} height={75} />
      ),
    },
    {
      title: "Tên sản phẩm",
      key: "productName",
      width: "20%",
      render: (_, productStock: IProductStock) =>
        productStock.product.productName,
    },
    {
      title: "Danh mục",
      // dataIndex: "category",
      key: "category",
      width: "15%",
      render: (_, record: IProductStock) =>
        record.product.category.categoryName,
      filters: categoryOptions,
      filteredValue: getDefaultFilterValue(searchParams, "categoryId")?.map(
        (categoryId) => Number(categoryId),
      ),
      filterIcon: (filtered: boolean) => {
        return <FilterFilled style={{ color: getFilterIconColor(filtered) }} />;
      },
    },
    {
      title: "Số lượng",
      dataIndex: "quantity",
      key: "quantity",
      width: "10%",
      sorter: true,
      defaultSortOrder: getDefaultSortOrder(searchParams, "quantity"),
      sortIcon: ({ sortOrder }) => (
        <div className="flex flex-col text-[10px]">
          <CaretUpFilled style={{ color: getSortUpIconColor(sortOrder) }} />
          <CaretDownFilled style={{ color: getSortDownIconColor(sortOrder) }} />
        </div>
      ),
    },
    {
      title: "Đơn vị tính",
      key: "unit",
      width: "10%",
      render: (_, productStock: IProductStock) =>
        productStock.product.unit.unitName,
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: IProductStock) => record.productStockId}
      pagination={tableParams.pagination}
      dataSource={productStockPage?.content}
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

export default ProductStockTable;
