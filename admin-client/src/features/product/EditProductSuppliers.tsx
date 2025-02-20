import { useNavigate, useParams } from "react-router";
import { useQuery } from "@tanstack/react-query";
import Loading from "../../common/components/Loading";
import { productService } from "../../services";
import { Button, Descriptions, DescriptionsProps, Space, Tooltip } from "antd";
import { GoArrowLeft } from "react-icons/go";
import UpdateProductSupplierForm from "./UpdateProductSupplierForm";

const EditProductSuppliers: React.FC = () => {
  const navigate = useNavigate();
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
          <Tooltip title="Quay lại">
            <Button icon={<GoArrowLeft />} onClick={() => navigate(-1)} />
          </Tooltip>
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
        <UpdateProductSupplierForm
          product={productData!.payload}
          onCancel={() => navigate(-1)}
        />
      </div>
    </div>
  );
};

export default EditProductSuppliers;
