import { Button, Space, Tooltip } from "antd";
import { GoArrowLeft } from "react-icons/go";
import { useNavigate, useParams } from "react-router";
import UpdateProductForm from "./UpdateProductForm";
import { useQuery } from "@tanstack/react-query";
import { productService } from "../../services";
import Loading from "../../common/components/Loading";

const ViewProduct: React.FC = () => {
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
          <Tooltip title="Quay lại">
            <Button icon={<GoArrowLeft />} onClick={() => navigate(-1)} />
          </Tooltip>
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
