import { useQuery } from "@tanstack/react-query";
import { Space } from "antd";
import { useNavigate, useParams } from "react-router";
import BackButton from "../../common/components/BackButton";
import Loading from "../../common/components/Loading";
import UpdateProductForm from "./UpdateProductForm";
import { productService } from "../../services";

const ViewProduct: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();

  const { data: productData, isLoading } = useQuery({
    queryKey: ["products", id],
    queryFn: () => productService.getById(id || ""),
    enabled: !!id,
  });

  if (isLoading) {
    return <Loading />;
  }

  return (
    <div className="card">
      <div className="mb-5 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <h2 className="text-xl font-semibold">Thông tin sản phẩm</h2>
        </Space>
      </div>
      <UpdateProductForm
        productToUpdate={productData?.payload}
        onCancel={() => navigate(-1)}
        viewOnly
      />
    </div>
  );
};

export default ViewProduct;
