import {
  Card,
  DatePicker,
  Form,
  FormInstance,
  Input,
  InputNumber,
  Modal,
  Select,
} from "antd";
import dayjs, { Dayjs } from "dayjs";
import { useShallow } from "zustand/react/shallow";
import { useEffect } from "react";
import SearchCustomerBar from "../customer/SearchCustomerBar";
import { CreateExportInvoiceRequest, ICustomer } from "../../../interfaces";
import {
  ExportInvoiceDetailState,
  useExportInvoiceStore,
} from "../../../store/export-invoice-store";
import { formatCurrency, parseCurrency } from "../../../utils/number";
import { DiscountType } from "../../../common/enums";
import { convertKeysToSnakeCase } from "../../../utils/data";

interface ExportInvoiceFormProps {
  form: FormInstance;
}

const ExportInvoiceForm: React.FC<ExportInvoiceFormProps> = ({ form }) => {
  const [modal, contextHolder] = Modal.useModal();
  const {
    warehouse,
    user,
    totalAmount,
    discountValue,
    discountType,
    vatRate,
    finalAmount,
    setDiscountValue,
    setDiscountType,
    setVatRate,
    exportInvoiceDetails,
  } = useExportInvoiceStore(
    useShallow((state) => ({
      warehouse: state.warehouse,
      user: state.user,
      totalAmount: state.totalAmount,
      discountValue: state.discountValue,
      discountType: state.discountType,
      vatRate: state.vatRate,
      finalAmount: state.finalAmount,
      setDiscountValue: state.setDiscountValue,
      setDiscountType: state.setDiscountType,
      setVatRate: state.setVatRate,
      exportInvoiceDetails: state.exportInvoiceDetails,
    })),
  );

  useEffect(() => {
    form.setFieldsValue({
      discountValue: discountValue,
      discountType: discountType,
      vatRate: vatRate,
      totalAmount: totalAmount,
      finalAmount: finalAmount,
    });
  }, [form, totalAmount, discountValue, discountType, vatRate, finalAmount]);

  function handleSelectCustomer(customer: ICustomer) {
    form.setFieldsValue({
      customer: customer,
    });
  }

  function handleFinish() {
    const exportInvoiceFormValues = form.getFieldsValue(true);
    if (!exportInvoiceFormValues.customer) {
      modal.error({
        title: "Lỗi",
        content: "Vui lòng chọn khách hàng",
      });
      return;
    }

    const newExportInvoice: CreateExportInvoiceRequest = {
      customerId: exportInvoiceFormValues.customer.customerId,
      warehouseId: warehouse.warehouseId,
      userId: user.userId,
      createdDate: exportInvoiceFormValues.createdDate,
      totalAmount: exportInvoiceFormValues.totalAmount,
      discountValue: exportInvoiceFormValues.discountValue,
      discountType: exportInvoiceFormValues.discountType,
      vatRate: exportInvoiceFormValues.vatRate,
      finalAmount: exportInvoiceFormValues.finalAmount,
      note: exportInvoiceFormValues.note,
      exportInvoiceDetails: exportInvoiceDetails.map(
        (detail: ExportInvoiceDetailState) => ({
          productId: detail.product.productId,
          productUnitId: detail.productUnit.productUnitId,
          quantity: detail.quantity,
          unitPrice: detail.unitPrice,
          detailBatches: detail.selectedBatches
            .filter((batch) => batch.quantity > 0)
            .map((selectedBatch) => ({
              batchId: selectedBatch.productBatch.batchId,
              quantity: selectedBatch.quantity,
              batchLocations: selectedBatch.selectedLocations
                .filter((selectedLocation) => selectedLocation.quantity > 0)
                .map((selectedLocation) => ({
                  batchLocationId: selectedLocation.location.batchLocationId!,
                  quantity: selectedLocation.quantity,
                })),
            })),
        }),
      ),
    };
    console.log(
      "New Export Invoice: ",
      convertKeysToSnakeCase(newExportInvoice),
    );
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
          createdDate: dayjs().tz().format("YYYY-MM-DD"),
          discountValue: 0,
          vatRate: 10,
          discountType: discountType,
          totalAmount: 0,
          finalAmount: 0,
        }}
      >
        <div className="flex items-center justify-between gap-6">
          <Card className="flex-1 self-stretch">
            <Form.Item label="Khách hàng">
              <SearchCustomerBar
                className="w-full"
                placeholder="Nhập tên, hoặc số điện thoại khách hàng"
                onSelect={handleSelectCustomer}
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
              label="Ngày lập phiếu"
              name="createdDate"
              getValueProps={(value: string) => ({
                value: value && dayjs(value),
              })}
              normalize={(value: Dayjs) =>
                value && value.tz().format("YYYY-MM-DD")
              }
            >
              <DatePicker disabled className="w-full" format="DD/MM/YYYY" />
            </Form.Item>
            <Form.Item label="Ghi chú" name="note">
              <Input.TextArea rows={2} />
            </Form.Item>
          </Card>

          <Card className="w-[25%] self-stretch">
            <Form.Item label="Tổng tiền hàng" name="totalAmount">
              <InputNumber
                className="right-aligned-number w-full"
                controls={false}
                readOnly
                value={totalAmount}
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                step={1000}
                min={0}
                // addonAfter={<div className="w-[55px]">VND</div>}
              />
            </Form.Item>
            <Form.Item label="Chiết khấu" name="discountValue">
              <InputNumber
                className="right-aligned-number w-full"
                controls={false}
                value={discountValue}
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                onChange={(value) => {
                  setDiscountValue(value as number);
                }}
                step={discountType === DiscountType.AMOUNT ? 1000 : 1}
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
                        value: DiscountType.PERCENTAGE,
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
              <Select
                className="w-full"
                value={vatRate}
                onChange={(value) => {
                  setVatRate(value);
                }}
                labelRender={(props) => (
                  <div className="text-right">{props.label}</div>
                )}
                options={[
                  { value: 0, label: "0%" },
                  { value: 5, label: "5%" },
                  { value: 8, label: "8%" },
                  { value: 10, label: "10%" },
                ]}
              />
            </Form.Item>
            <Form.Item label="Tổng cộng" name="finalAmount">
              <InputNumber
                className="right-aligned-number w-full"
                controls={false}
                readOnly
                value={finalAmount}
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                min={0}
                // addonAfter={<div className="w-[55px]">VND</div>}
              />
            </Form.Item>
          </Card>
        </div>
      </Form>
    </>
  );
};

export default ExportInvoiceForm;
