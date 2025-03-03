import { UseMutateFunction } from "@tanstack/react-query";
import {
  Card,
  DatePicker,
  Form,
  Input,
  InputNumber,
  Modal,
  Select,
} from "antd";
import { FormInstance } from "antd/lib";
import dayjs, { Dayjs } from "dayjs";
import toast from "react-hot-toast";
import { useShallow } from "zustand/react/shallow";
import { DiscountType, PurchaseOrderStatus } from "../../common/enums";
import {
  ApiResponse,
  CreatePurchaseOrderRequest,
  ISupplier,
} from "../../interfaces";
import {
  PurchaseOrderDetailState,
  usePurchaseOrderStore,
} from "../../store/purchase-order-store";
import { getNotificationMessage } from "../../utils/notification";
import { formatCurrency, parseCurrency } from "../../utils/number";
import SearchSupplierBar from "../supplier/SearchSupplierBar";

interface PurchaseOrderFormProps {
  form: FormInstance;
  createPurchaseOrder: UseMutateFunction<
    ApiResponse<void>,
    Error,
    CreatePurchaseOrderRequest,
    unknown
  >;
}

const PurchaseOrderForm: React.FC<PurchaseOrderFormProps> = ({
  form,
  createPurchaseOrder,
}) => {
  const [modal, contextHolder] = Modal.useModal();
  const {
    warehouse,
    user,
    discountType,
    setDiscountType,
    purchaseOrderDetails,
  } = usePurchaseOrderStore(
    useShallow((state) => ({
      warehouse: state.warehouse,
      user: state.user,
      discountType: state.discountType,
      setDiscountType: state.setDiscountType,
      purchaseOrderDetails: state.purchaseOrderDetails,
    })),
  );

  function handleSelectSupplier(supplier: ISupplier) {
    form.setFieldsValue({
      supplier: supplier,
    });
  }

  function handleFinish() {
    const purchaseOrderFormValues = form.getFieldsValue(true);
    if (!purchaseOrderFormValues.supplier) {
      modal.error({
        title: "Lỗi",
        content: "Vui lòng chọn nhà cung cấp",
      });
      return;
    }
    const newPurchaseOrder: CreatePurchaseOrderRequest = {
      supplierId: purchaseOrderFormValues.supplier.supplierId as string,
      warehouseId: warehouse.warehouseId,
      userId: user.userId,
      orderDate: purchaseOrderFormValues.orderDate as string,
      status: PurchaseOrderStatus.PENDING,
      expectedDeliveryDate:
        purchaseOrderFormValues.expectedDeliveryDate as string,
      note: purchaseOrderFormValues.note as string,
      totalAmount: purchaseOrderFormValues.totalAmount as number,
      discountValue: purchaseOrderFormValues.discountValue as number,
      discountType: purchaseOrderFormValues.discountType as DiscountType,
      vatRate: purchaseOrderFormValues.vatRate as number,
      finalAmount: purchaseOrderFormValues.finalAmount as number,
      purchaseOrderDetails: purchaseOrderDetails.map(
        (detail: PurchaseOrderDetailState) => ({
          productId: detail.product.productId,
          productUnitId: detail.productUnit.productUnitId,
          quantity: detail.quantity,
        }),
      ),
    };
    createPurchaseOrder(newPurchaseOrder, {
      onSuccess: () => {
        toast.success("Đã tạo đơn đặt hàng");
      },
      onError: (error) => {
        toast.error(
          getNotificationMessage(error) || "Tạo đơn đặt hàng thất bại",
        );
      },
    });
  }

  return (
    <>
      {contextHolder}
      <Form
        form={form}
        className="mb-6"
        onFinish={handleFinish}
        layout="vertical"
        initialValues={{
          orderDate: dayjs().tz().format("YYYY-MM-DD"),
          discountValue: 0,
          vatRate: 0,
          discountType: discountType,
          totalAmount: 0,
          finalAmount: 0,
        }}
      >
        <div className="flex items-center justify-between gap-6">
          <Card className="flex-1 self-stretch">
            <Form.Item label="Nhà cung cấp">
              <SearchSupplierBar
                className="w-full"
                placeholder="Nhập tên, hoặc số điện thoại nhà cung cấp"
                onSelect={handleSelectSupplier}
              />
            </Form.Item>
            <Form.Item label="Kho nhận hàng">
              <Input value={warehouse.warehouseName} readOnly />
            </Form.Item>
            <Form.Item label="Người tạo">
              <Input value={user.fullName} readOnly />
            </Form.Item>
          </Card>
          <Card className="flex-1 self-stretch">
            <Form.Item
              label="Ngày đặt hàng"
              name="orderDate"
              getValueProps={(value: string) => ({
                value: value && dayjs(value),
              })}
              normalize={(value: Dayjs) =>
                value && value.tz().format("YYYY-MM-DD")
              }
            >
              <DatePicker disabled className="w-full" format="DD/MM/YYYY" />
            </Form.Item>
            <Form.Item
              label="Ngày dự kiến nhận hàng"
              name="expectedDeliveryDate"
              rules={[
                {
                  required: true,
                  message: "Vui lòng chọn ngày dự kiến nhận hàng",
                },
              ]}
              getValueProps={(value: string) => ({
                value: value && dayjs(value),
              })}
              normalize={(value: Dayjs) =>
                value && value.tz().format("YYYY-MM-DD")
              }
            >
              <DatePicker
                className="w-full"
                format="DD/MM/YYYY"
                placeholder=""
                disabledDate={(current) => {
                  return dayjs(current).isBefore(dayjs().startOf("day"));
                }}
                onChange={(date: Dayjs) => {
                  form.setFieldsValue({
                    expectedDeliveryDate: date.tz().format("YYYY-MM-DD"),
                  });
                }}
              />
            </Form.Item>
            <Form.Item label="Ghi chú" name="note">
              <Input.TextArea rows={2} />
            </Form.Item>
          </Card>
          <Card className="flex-1 self-stretch">
            <Form.Item label="Tổng tiền hàng" name="totalAmount">
              <InputNumber
                readOnly
                className="w-full"
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                step={1000}
                min={0}
                addonAfter="VND"
              />
            </Form.Item>
            <Form.Item label="Chiết khấu" name="discountValue">
              <InputNumber
                className="w-full"
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                step={1000}
                min={0}
                addonAfter={
                  <Select
                    value={discountType}
                    style={{ width: "75px" }}
                    dropdownStyle={{
                      minWidth: "75px",
                    }}
                    options={[
                      {
                        value: DiscountType.AMOUNT,
                        label: "VND",
                      },
                      {
                        value: DiscountType.PERCENT,
                        label: "%",
                      },
                    ]}
                    onChange={(value) => {
                      setDiscountType(value);
                    }}
                  />
                }
              />
            </Form.Item>
            <Form.Item label="VAT" name="vatRate">
              <InputNumber
                className="w-full"
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                min={0}
                addonAfter="%"
              />
            </Form.Item>
            <Form.Item label="Tổng cộng" name="finalAmount">
              <InputNumber
                readOnly
                className="w-full"
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                min={0}
                addonAfter="VND"
              />
            </Form.Item>
          </Card>
        </div>
      </Form>
    </>
  );
};

export default PurchaseOrderForm;
