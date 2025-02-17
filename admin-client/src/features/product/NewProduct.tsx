import { Button, Space, Tooltip } from "antd";
import { GoArrowLeft } from "react-icons/go";
import { useNavigate } from "react-router";
import UpdateProductForm from "./UpdateProductForm";

const NewProduct: React.FC = () => {
  const navigate = useNavigate();
  return (
    <div className="card">
      <div className="mb-5 flex items-center justify-between">
        <Space align="start" size="middle">
          <Tooltip title="Quay lại">
            <Button icon={<GoArrowLeft />} onClick={() => navigate(-1)} />
          </Tooltip>
          <h2 className="text-xl font-semibold">Thêm sản phẩm mới</h2>
        </Space>
      </div>
      <UpdateProductForm onCancel={() => navigate(-1)} />
    </div>
  );
};

export default NewProduct;
