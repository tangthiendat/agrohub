import { useQuery } from "@tanstack/react-query";
import { Button, Space } from "antd";
import { useNavigate, useParams } from "react-router";
import BackButton from "../../../common/components/BackButton";
import Loading from "../../../common/components/Loading";
import UpdateProductForm from "./UpdateProductForm";
import { useTitle } from "../../../common/hooks";
import { productService } from "../../../services";

const EditProduct: React.FC = () => {
  useTitle("Chỉnh sửa sản phẩm");
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

  return (
    <div className="card">
      <div className="mb-5 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <h2 className="text-xl font-semibold">Chỉnh sửa sản phẩm</h2>
        </Space>
        <Button type="primary" onClick={() => navigate("suppliers")}>
          Danh sách nhà cung cấp
        </Button>
      </div>
      <UpdateProductForm
        productToUpdate={productData?.payload}
        onCancel={() => navigate(-1)}
      />
    </div>
  );
};

export default EditProduct;
