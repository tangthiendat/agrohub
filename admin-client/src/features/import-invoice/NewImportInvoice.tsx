import { ReloadOutlined, SaveFilled } from "@ant-design/icons";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Button, Form, Space } from "antd";
import { useEffect } from "react";
import toast from "react-hot-toast";
import { useShallow } from "zustand/react/shallow";
import BackButton from "../../common/components/BackButton";
import Loading from "../../common/components/Loading";
import { useCurrentWarehouse } from "../../common/hooks";
import { useCurrentUserInfo } from "../../common/hooks/useCurrentUserInfo";
import { IProduct } from "../../interfaces";
import { importInvoiceService } from "../../services";
import { useImportInvoiceStore } from "../../store/import-invoice-store";
import SearchProductBar from "../product/SearchProductBar";
import ImportInvoiceDetailsTable from "./ImportInvoiceDetailsTable";
import ImportInvoiceForm from "./ImportInvoiceForm";

const NewImportInvoice: React.FC = () => {
  const [form] = Form.useForm();
  const queryClient = useQueryClient();
  const { addDetail, setWarehouse, setUser, reset } = useImportInvoiceStore(
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

  const { currentWarehouse, isLoading: isWarehouseLoading } =
    useCurrentWarehouse();

  const { currentUserInfo, isLoading: isUserLoading } = useCurrentUserInfo();

  const { mutate: createImportInvoice, isPending: isCreating } = useMutation({
    mutationFn: importInvoiceService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["import-invoices"],
      });
      queryClient.invalidateQueries({
        queryKey: ["product-batches"],
      });
      form.resetFields();
      reset();
    },
  });

  useEffect(() => {
    if (currentWarehouse) {
      setWarehouse(currentWarehouse);
    }
    if (currentUserInfo) {
      setUser(currentUserInfo);
    }
  }, [currentWarehouse, currentUserInfo, setWarehouse, setUser]);

  if (isWarehouseLoading || isUserLoading) {
    return <Loading />;
  }

  return (
    <div className="card">
      <div className="mb-4 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <h2 className="text-xl font-semibold">Thêm phiếu nhập kho</h2>
        </Space>
        <Space>
          <Button
            icon={<ReloadOutlined />}
            onClick={() => {
              reset();
              toast.success("Làm mới thành công");
            }}
            loading={isCreating}
          >
            Làm mới
          </Button>
          <Button
            icon={<SaveFilled />}
            type="primary"
            onClick={() => form.submit()}
            loading={isCreating}
          >
            Lưu
          </Button>
        </Space>
      </div>

      <ImportInvoiceForm
        form={form}
        createImportInvoice={createImportInvoice}
      />

      <div className="flex items-center justify-between">
        <SearchProductBar
          className="mb-6 w-[40%]"
          placeholder="Nhập tên hoặc mã sản phẩm"
          onSelect={handleSelectProduct}
          optionRenderer={(product) => ({
            value: product.productId,
            label: (
              <div className="flex items-center gap-2">
                <img
                  src={product.imageUrl}
                  alt={product.productName}
                  className="h-10 w-10 object-cover"
                />
                <div className="flex-1">
                  <div className="flex items-center justify-between gap-2 font-semibold">
                    <div className="text-wrap">{product.productName} </div>
                    <div className="bg-sky-100 px-[3px] py-[3px] text-sky-600">
                      {
                        product.productUnits.find((unit) => unit.isDefault)!
                          .unit.unitName
                      }
                    </div>
                  </div>
                  <div>
                    <div className="text-gray-500">ID: {product.productId}</div>
                  </div>
                </div>
              </div>
            ),
          })}
        />
      </div>
      <ImportInvoiceDetailsTable form={form} />
    </div>
  );
};

export default NewImportInvoice;
