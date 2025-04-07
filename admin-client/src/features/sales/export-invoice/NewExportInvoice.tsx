import { useQuery, useQueryClient } from "@tanstack/react-query";
import { ReloadOutlined, SaveFilled } from "@ant-design/icons";
import { Button, Form, Modal, Space } from "antd";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useShallow } from "zustand/react/shallow";
import BackButton from "../../../common/components/BackButton";
import Loading from "../../../common/components/Loading";
import SearchProductBar from "../../item/product/SearchProductBar";
import ExportInvoiceForm from "./ExportInvoiceForm";
import ExportInvoiceDetailsTable from "./ExportInvoiceDetailsTable";
import { useCurrentUserInfo, useCurrentWarehouse } from "../../../common/hooks";
import { IProduct } from "../../../interfaces";
import { useExportInvoiceStore } from "../../../store/export-invoice-store";
import { productBatchService } from "../../../services";

const NewExportInvoice: React.FC = () => {
  const [form] = Form.useForm();
  const queryClient = useQueryClient();
  const [modal, contextHolder] = Modal.useModal();
  const {
    addDetail,
    initDetailBatch,
    setWarehouse,
    setUser,
    reset,
    exportInvoiceDetails,
  } = useExportInvoiceStore(
    useShallow((state) => ({
      setWarehouse: state.setWarehouse,
      setUser: state.setUser,
      addDetail: state.addDetail,
      initDetailBatch: state.initDetailBatch,
      reset: state.reset,
      exportInvoiceDetails: state.exportInvoiceDetails,
    })),
  );
  const [currentProductId, setCurrentProductId] = useState<string | undefined>(
    undefined,
  );

  const { data: productBatches } = useQuery({
    queryKey: ["product-batches", "product", currentProductId],
    queryFn: () => productBatchService.getByProductId(currentProductId!),
    enabled: !!currentProductId,
    select: (data) => data.payload,
  });

  useEffect(() => {
    if (productBatches) {
      initDetailBatch(currentProductId!, productBatches);
    }
  }, [productBatches, initDetailBatch, currentProductId]);

  function handleSelectProduct(product: IProduct) {
    const productExists = exportInvoiceDetails.some(
      (detail) => detail.product.productId === product.productId,
    );

    if (productExists) {
      modal.warning({
        title: "Sản phẩm đã tồn tại",
        content: `Sản phẩm đã có trong danh sách chi tiết phiếu xuất kho.`,
      });
    } else {
      setCurrentProductId(product.productId);
      addDetail(product);
    }
  }

  const { currentWarehouse, isLoading: isWarehouseLoading } =
    useCurrentWarehouse();

  const { currentUserInfo, isLoading: isUserLoading } = useCurrentUserInfo();

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
    <>
      {contextHolder}
      <div className="card">
        <div className="mb-4 flex items-center justify-between">
          <Space align="start" size="middle">
            <BackButton />
            <h2 className="text-xl font-semibold">Thêm phiếu xuất kho</h2>
          </Space>
          <Space>
            <Button
              icon={<ReloadOutlined />}
              onClick={() => {
                reset();
                queryClient.removeQueries({
                  queryKey: ["product-batches", "product"],
                });
                toast.success("Làm mới thành công");
              }}
              // loading={isCreating}
            >
              Làm mới
            </Button>
            <Button
              icon={<SaveFilled />}
              type="primary"
              onClick={() => form.submit()}
              // loading={isCreating}
            >
              Lưu
            </Button>
          </Space>
        </div>
        <ExportInvoiceForm form={form} />

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
                      <div className="text-gray-500">
                        ID: {product.productId}
                      </div>
                    </div>
                  </div>
                </div>
              ),
            })}
          />
        </div>

        <ExportInvoiceDetailsTable
          form={form}
          setCurrentProductId={setCurrentProductId}
        />
      </div>
    </>
  );
};

export default NewExportInvoice;
