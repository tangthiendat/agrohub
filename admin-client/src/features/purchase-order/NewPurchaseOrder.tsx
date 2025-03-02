import { useEffect } from "react";
import toast from "react-hot-toast";
import { useQuery } from "@tanstack/react-query";
import { Button, Form, Space } from "antd";
import { useShallow } from "zustand/react/shallow";
import BackButton from "../../common/components/BackButton";
import Loading from "../../common/components/Loading";
import SearchProductBar from "../product/SearchProductBar";
import PurchaseOrderDetailsTable from "./PurchaseOrderDetailsTable";
import PurchaseOrderForm from "./PurchaseOrderForm";
import { usePurchaseOrderStore } from "../../store/purchase-order-store";
import { IProduct } from "../../interfaces";
import { userService, warehouseService } from "../../services";

const NewPurchaseOrder: React.FC = () => {
  const [form] = Form.useForm();
  const { addDetail, setWarehouse, setUser, reset } = usePurchaseOrderStore(
    useShallow((state) => ({
      addDetail: state.addDetail,
      setWarehouse: state.setWarehouse,
      setUser: state.setUser,
      reset: state.reset,
    })),
  );

  function handleSelectProduct(product: IProduct) {
    addDetail(product);
  }

  const { data: warehouseData, isLoading: isWarehouseLoading } = useQuery({
    queryKey: ["warehouse", "me"],
    queryFn: warehouseService.getCurrentUserWarehouse,
  });

  const { data: userData, isLoading: isUserLoading } = useQuery({
    queryKey: ["user", "info"],
    queryFn: userService.getUserInfo,
  });

  useEffect(() => {
    if (warehouseData) {
      setWarehouse(warehouseData.payload);
    }
    if (userData) {
      setUser(userData.payload);
    }
  }, [warehouseData, userData, setWarehouse, setUser]);

  if (isWarehouseLoading || isUserLoading) {
    return <Loading />;
  }

  return (
    <div className="card">
      <div className="mb-4 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <h2 className="text-xl font-semibold">
            Thêm đơn đặt hàng nhà cung cấp
          </h2>
        </Space>
        <Space>
          <Button
            onClick={() => {
              reset();
              toast.success("Đã xóa đơn đặt hàng");
            }}
          >
            Làm mới
          </Button>
          <Button type="primary" onClick={() => form.submit()}>
            Lưu
          </Button>
        </Space>
      </div>

      <PurchaseOrderForm form={form} />

      <div className="flex items-center justify-between">
        <SearchProductBar
          className="mb-6 w-[40%]"
          placeholder="Nhập tên hoặc mã sản phẩm"
          onSelect={handleSelectProduct}
          optionRenderer={(product) => ({
            value: product.productId,
            label: (
              <div>
                <div className="flex items-center justify-between gap-2 font-semibold">
                  <div className="text-wrap">{product.productName} </div>
                  <div className="bg-sky-100 px-[3px] py-[3px] text-sky-600">
                    {
                      product.productUnits.find((unit) => unit.isDefault)!.unit
                        .unitName
                    }
                  </div>
                </div>
                <div>
                  <div className="text-gray-500">ID: {product.productId}</div>
                </div>
              </div>
            ),
          })}
        />
      </div>
      <PurchaseOrderDetailsTable />
    </div>
  );
};

export default NewPurchaseOrder;
