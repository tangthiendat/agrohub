import { useEffect } from "react";
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
import {
  ImportInvoiceDetailState,
  useImportInvoiceStore,
} from "../../store/import-invoice-store";
import { useShallow } from "zustand/react/shallow";
import dayjs, { Dayjs } from "dayjs";
import SearchSupplierBar from "../supplier/SearchSupplierBar";
import { CreateImportInvoiceRequest, ISupplier } from "../../interfaces";
import { DiscountType } from "../../common/enums";
import { convertKeysToSnakeCase } from "../../utils/data";
import { formatCurrency, parseCurrency } from "../../utils/number";

interface ImportInvoiceFormProps {
  form: FormInstance;
}

const ImportInvoiceForm: React.FC<ImportInvoiceFormProps> = ({ form }) => {
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
    importInvoiceDetails,
  } = useImportInvoiceStore(
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
      importInvoiceDetails: state.importInvoiceDetails,
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

  function handleSelectSupplier(supplier: ISupplier) {
    form.setFieldsValue({
      supplier: supplier,
    });
  }

  function handleFinish() {
    const importInvoiceFormValues = form.getFieldsValue(true);
    if (!importInvoiceFormValues.supplier) {
      modal.error({
        title: "Lỗi",
        content: "Vui lòng chọn nhà cung cấp",
      });
      return;
    }
    const newImportInvoice: CreateImportInvoiceRequest = {
      supplierId: importInvoiceFormValues.supplier.supplierId as string,
      warehouseId: warehouse.warehouseId,
      userId: user.userId,
      createdDate: importInvoiceFormValues.createdDate as string,
      totalAmount: importInvoiceFormValues.totalAmount as number,
      discountValue: importInvoiceFormValues.discountValue as number,
      discountType: importInvoiceFormValues.discountType as DiscountType,
      vatRate: importInvoiceFormValues.vatRate as number,
      finalAmount: importInvoiceFormValues.finalAmount as number,
      importInvoiceDetails: importInvoiceDetails.map(
        (detail: ImportInvoiceDetailState) => ({
          productId: detail.product.productId,
          productUnitId: detail.productUnit.productUnitId,
          quantity: detail.quantity,
          unitPrice: detail.unitPrice,
        }),
      ),
      note: importInvoiceFormValues.note as string,
    };
    console.log(convertKeysToSnakeCase(newImportInvoice));
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
          </Card>

          <Card className="flex-1 self-stretch">
            <Form.Item label="Tổng tiền hàng" name="totalAmount">
              <InputNumber
                readOnly
                value={totalAmount}
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
                value={discountValue}
                className="w-full"
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
              <InputNumber
                value={vatRate}
                className="w-full"
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                onChange={(value) => {
                  setVatRate(value as number);
                }}
                min={0}
                addonAfter="%"
              />
            </Form.Item>
            <Form.Item label="Tổng cộng" name="finalAmount">
              <InputNumber
                readOnly
                value={finalAmount}
                className="w-full"
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                min={0}
                addonAfter="VND"
              />
            </Form.Item>
          </Card>
          <Card className="flex-1 self-stretch">
            <Form.Item label="Ghi chú" name="note">
              <Input.TextArea rows={2} />
            </Form.Item>
          </Card>
        </div>
      </Form>
    </>
  );
};

export default ImportInvoiceForm;
