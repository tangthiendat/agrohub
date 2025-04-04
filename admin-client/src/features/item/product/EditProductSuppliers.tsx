import { useQuery } from "@tanstack/react-query";
import { Descriptions, DescriptionsProps, Space } from "antd";
import { useParams } from "react-router";
import BackButton from "../../../common/components/BackButton";
import Loading from "../../../common/components/Loading";
import ProductSupplierTable from "./ProductSupplierTable";
import UpdateProductSupplierForm from "./UpdateProductSupplierForm";
import { productService } from "../../../services";

const EditProductSuppliers: React.FC = () => {
  const { id } = useParams<{ id: string }>();

  const { data: productData, isLoading } = useQuery({
    queryKey: ["product", id],
    queryFn: () => productService.getById(id || ""),
    enabled: !!id,
  });

  if (isLoading) {
    return <Loading />;
  }

  const items: DescriptionsProps["items"] = [
    {
      label: "Mã sản phẩm",
      key: "productId",
      children: (
        <span className="font-semibold">{productData?.payload.productId}</span>
      ),
    },
    {
      label: "Tên sản phẩm",
      key: "productName",
      children: (
        <span className="font-semibold">
          {productData?.payload.productName}
        </span>
      ),
    },
  ];

  return (
    <div className="card">
      <div className="mb-5 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <h2 className="text-xl font-semibold">
            Chỉnh sửa danh sách nhà cung cấp
          </h2>
        </Space>
      </div>
      <Descriptions
        bordered
        size="small"
        items={items}
        labelStyle={{
          width: "15%",
        }}
      />
      <div className="mt-6">
        <UpdateProductSupplierForm product={productData!.payload} />
      </div>
      <div className="mt-6">
        <ProductSupplierTable productId={id!} />
      </div>
    </div>
  );
};

export default EditProductSuppliers;
