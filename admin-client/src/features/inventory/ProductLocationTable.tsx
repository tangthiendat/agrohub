import { TableProps } from "antd/lib";
import { Table, TablePaginationConfig, Tag } from "antd";
import { useSearchParams } from "react-router";
import { useEffect, useState } from "react";
import { IProductLocation, Page } from "../../interfaces";
import { getSortDirection } from "../../utils/filter";
import { LocationStatus, RackType } from "../../common/enums";
import {
  LOCATION_STATUS_COLOR,
  LOCATION_STATUS_NAME,
  RACK_TYPE_NAME,
} from "../../common/constants";

interface ProductLocationTableProps {
  productLocationPage?: Page<IProductLocation>;
  isLoading: boolean;
}

interface TableParams {
  pagination: TablePaginationConfig;
}

const ProductLocationTable: React.FC<ProductLocationTableProps> = ({
  productLocationPage,
  isLoading,
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tableParams, setTableParams] = useState<TableParams>(() => ({
    pagination: {
      current: Number(searchParams.get("page")) || 1,
      pageSize: Number(searchParams.get("pageSize")) || 10,
      showSizeChanger: true,
      showTotal: (total) => `Tổng ${total} vị trí sản phẩm`,
    },
  }));

  useEffect(() => {
    if (productLocationPage) {
      setTableParams((prev) => ({
        ...prev,
        pagination: {
          ...prev.pagination,
          total: productLocationPage.meta?.totalElements || 0,
          showTotal: (total) => `Tổng ${total} vị trí sản phẩm`,
        },
      }));
    }
  }, [productLocationPage]);

  const handleTableChange: TableProps<IProductLocation>["onChange"] = (
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

  const columns: TableProps<IProductLocation>["columns"] = [
    {
      title: "Loại kệ hàng",
      dataIndex: "rackType",
      key: "rackType",
      width: "15%",
      render: (rackType: RackType) => RACK_TYPE_NAME[rackType],
    },
    {
      title: "Tên kệ hàng",
      dataIndex: "rackName",
      key: "rackName",
      width: "15%",
    },
    {
      title: "Ngăn",
      dataIndex: "rowNumber",
      key: "rowNumber",
      width: "15%",
    },
    {
      title: "Tầng",
      dataIndex: "columnNumber",
      key: "columnNumber",
      width: "15%",
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      width: "15%",
      render: (status: LocationStatus) => (
        <Tag color={LOCATION_STATUS_COLOR[status]}>
          {LOCATION_STATUS_NAME[status]}
        </Tag>
      ),
    },
  ];

  return (
    <Table
      bordered={false}
      columns={columns}
      rowKey={(record: IProductLocation) => record.locationId}
      pagination={tableParams.pagination}
      dataSource={productLocationPage?.content}
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

export default ProductLocationTable;
