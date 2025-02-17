import {
  Button,
  DatePicker,
  Form,
  FormInstance,
  InputNumber,
  Space,
} from "antd";
import dayjs, { Dayjs } from "dayjs";
import { IProduct, IProductUnit, IProductUnitPrice } from "../../interfaces";
import { formatCurrency, parseCurrency } from "../../utils/number";
import { useProductUnitPrice } from "../../context/ProductUnitPriceContext";
import { useEffect } from "react";

interface UpdateProductUnitPriceFormProps {
  productForm: FormInstance<IProduct>;
  productUnitIndex: number;
  onCancel: () => void;
}

const UpdateProductUnitPriceForm: React.FC<UpdateProductUnitPriceFormProps> = ({
  productForm,
  productUnitIndex,
  onCancel,
}) => {
  const [form] = Form.useForm<IProductUnitPrice>();
  const { currentProductUnitPrice, setCurrentProductUnitPrice } =
    useProductUnitPrice();

  useEffect(() => {
    if (currentProductUnitPrice) {
      form.setFieldsValue(currentProductUnitPrice);
    }
  }, [form, currentProductUnitPrice]);

  function updateProductUnitPrices(
    productUnit: IProductUnit,
    newPrice: IProductUnitPrice,
  ): IProductUnitPrice[] {
    const productUnitPrices = productUnit?.productUnitPrices || [];

    if (productUnitPrices.length === 0) {
      return [newPrice];
    }

    const newPUPriceIndex = productUnitPrices.findIndex(
      (pup) =>
        !pup?.productUnitPriceId &&
        (pup.validTo == null || pup.validTo === undefined),
    );
    // if already had newPUPrice, replace it
    // else push newPrice to the end of the array
    if (newPUPriceIndex !== -1) {
      //set validTo for the last price if there is a last price
      if (newPUPriceIndex > 0) {
        productUnitPrices[newPUPriceIndex - 1].validTo = newPrice.validFrom;
      }

      productUnitPrices[newPUPriceIndex] = newPrice;
    } else {
      //set validTo for the last price
      const lastPUPriceIndex = productUnitPrices.findIndex(
        (pup) => pup.validTo == null || pup.validTo === undefined,
      );
      if (lastPUPriceIndex !== -1) {
        productUnitPrices[lastPUPriceIndex].validTo = newPrice.validFrom;
      }
      productUnitPrices.push(newPrice);
    }
    return productUnitPrices;
  }

  function handleFinish(values: IProductUnitPrice) {
    const product = productForm.getFieldsValue();
    setCurrentProductUnitPrice(values);
    productForm.setFieldsValue({
      ...product,
      productUnits: product.productUnits.map(
        (productUnit: IProductUnit, index: number) =>
          index === productUnitIndex
            ? {
                ...productUnit,
                productUnitPrices: updateProductUnitPrices(productUnit, values),
              }
            : productUnit,
      ),
    });

    onCancel();
  }

  return (
    <Form onFinish={handleFinish} form={form} layout="vertical">
      <Form.Item
        label="Giá"
        name="price"
        rules={[
          {
            required: true,
            message: "Hãy nhập giá",
          },
        ]}
      >
        <InputNumber
          className="w-full"
          formatter={(value) => formatCurrency(value)}
          parser={(value) => parseCurrency(value) as unknown as 0}
          step={1000}
          min={0}
          max={1000000000}
          addonAfter="VND"
        />
      </Form.Item>
      <Form.Item
        label="Ngày hiệu lực"
        name="validFrom"
        rules={[
          {
            required: true,
            message: "Hãy chọn ngày hiệu lực",
          },
        ]}
        getValueProps={(value: string) => ({
          value: value && dayjs(value),
        })}
        normalize={(value: Dayjs) => value && value.tz().format("YYYY-MM-DD")}
      >
        <DatePicker
          className="w-full"
          format="DD/MM/YYYY"
          placeholder="Chọn ngày hiệu lực"
        />
      </Form.Item>
      <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
        <Space>
          <Button onClick={onCancel}>Hủy</Button>
          <Button type="primary" htmlType="submit">
            Lưu
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UpdateProductUnitPriceForm;
