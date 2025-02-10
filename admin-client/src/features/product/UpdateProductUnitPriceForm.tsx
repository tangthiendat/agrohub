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
  }, [currentProductUnitPrice]);

  function handleFinish(values: IProductUnitPrice) {
    const product = productForm.getFieldsValue();
    setCurrentProductUnitPrice(values);
    productForm.setFieldsValue({
      ...product,
      productUnits: product.productUnits.map(
        (productUnit: IProductUnit, index: number) => {
          if (index === productUnitIndex) {
            return {
              ...productUnit,
              productUnitPrices:
                productUnit.productUnitPrices &&
                productUnit.productUnitPrices.length > 0
                  ? productUnit.productUnitPrices.map(
                      (productUnitPrice: IProductUnitPrice, index: number) => {
                        if (
                          index ===
                          productUnit.productUnitPrices.length - 1
                        ) {
                          return values;
                        }
                        return productUnitPrice;
                      },
                    )
                  : [values],
            };
          }
          return productUnit;
        },
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
