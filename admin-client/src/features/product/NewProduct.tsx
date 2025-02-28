import { Space } from "antd";
import BackButton from "../../common/components/BackButton";
import UpdateProductForm from "./UpdateProductForm";

const NewProduct: React.FC = () => {
  return (
    <div className="card">
      <div className="mb-5 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <h2 className="text-xl font-semibold">Thêm sản phẩm mới</h2>
        </Space>
      </div>
      <UpdateProductForm />
    </div>
  );
};

export default NewProduct;
