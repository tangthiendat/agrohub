import { useQueries, useQuery } from "@tanstack/react-query";
import {
  Card,
  DatePicker,
  Form,
  Input,
  InputNumber,
  Select,
  Space,
  Table,
  Tag,
} from "antd";
import dayjs, { Dayjs } from "dayjs";
import { useEffect, useState } from "react";
import { useParams } from "react-router";
import BackButton from "../../common/components/BackButton";
import Loading from "../../common/components/Loading";
import {
  PURCHASE_ORDER_STATUS_COLOR,
  PURCHASE_ORDER_STATUS_NAME,
} from "../../common/constants";
import { DiscountType, PurchaseOrderStatus } from "../../common/enums";
import {
  IProduct,
  IProductUnit,
  IPurchaseOrder,
  IPurchaseOrderDetail,
} from "../../interfaces";
import { productService, purchaseOrderService } from "../../services";
import { formatCurrency, parseCurrency } from "../../utils/number";
import { TableProps } from "antd/lib";

const ViewPurchaseOrder: React.FC = () => {
  const { id } = useParams();
  const [currentPurchaseOrder, setCurrentPurchaseOrder] = useState<
    IPurchaseOrder | undefined
  >(undefined);
  const [form] = Form.useForm<IPurchaseOrder>();
  const { data: purchaseOrder, isLoading: isPurchaseOrderLoading } = useQuery({
    queryKey: ["purchase-orders", id],
    queryFn: () => purchaseOrderService.getById(id!),
    enabled: !!id,
    select: (data) => data.payload,
  });

  const { productsMap, isProductLoading } = useQueries({
    queries:
      purchaseOrder?.purchaseOrderDetails.map((pod) => ({
        queryKey: ["products", pod.product.productId],
        queryFn: () => productService.getById(pod.product.productId),
        enabled: !!purchaseOrder,
      })) || [],
    combine: (results) => {
      const productsMap = results.reduce(
        (acc, result) => {
          if (result.data?.payload) {
            acc[result.data.payload.productId] = result.data.payload;
          }
          return acc;
        },
        {} as Record<string, IProduct>,
      );

      return {
        productsMap: productsMap,
        isProductLoading: results.some((result) => result.isLoading),
      };
    },
  });

  useEffect(() => {
    if (purchaseOrder && productsMap) {
      const fullPurchaseOrder = {
        ...purchaseOrder,

        purchaseOrderDetails: purchaseOrder.purchaseOrderDetails.map((pod) => ({
          ...pod,
          product: productsMap[pod.product.productId],
        })),
      };
      setCurrentPurchaseOrder(fullPurchaseOrder);
      form.setFieldsValue(fullPurchaseOrder);
    }
  }, [purchaseOrder, productsMap, form]);

  if (isPurchaseOrderLoading || isProductLoading) {
    return <Loading />;
  }

  const columns: TableProps<IPurchaseOrderDetail>["columns"] = [
    {
      title: "STT",
      width: "5%",
      key: "index",
      render: (_, __, index: number) => index + 1,
    },
    {
      title: "Mã sản phẩm",
      dataIndex: "product",
      key: "productId",
      width: "10%",
      render: (product) => product?.productId,
    },
    {
      title: "Tên sản phẩm",
      dataIndex: "product",
      key: "productName",
      width: "25%",
      render: (product) => product?.productName,
    },
    {
      title: "Đơn vị tính",
      dataIndex: "productUnit",
      key: "unit",
      width: "15%",
      render: (productUnit: IProductUnit, record: IPurchaseOrderDetail) => (
        <Select
          disabled
          value={productUnit.productUnitId}
          options={record.product?.productUnits?.map((pu) => ({
            value: pu.productUnitId,
            label: pu.unit.unitName,
          }))}
        />
      ),
    },
    {
      title: "Số lượng",
      dataIndex: "quantity",
      key: "quantity",
      width: "15%",
      render: (quantity: number) => (
        <InputNumber value={quantity} min={1} readOnly />
      ),
    },
    ...(currentPurchaseOrder?.status === PurchaseOrderStatus.APPROVED ||
    currentPurchaseOrder?.status === PurchaseOrderStatus.COMPLETED
      ? [
          {
            title: "Đơn giá",
            dataIndex: "productUnit",
            key: "unitPrice",
            width: "15%",
            render: (_: IProductUnit, record: IPurchaseOrderDetail) => {
              const currentPODetail =
                currentPurchaseOrder?.purchaseOrderDetails.find(
                  (pod) => pod.product?.productId === record.product?.productId,
                );
              return (
                <InputNumber
                  value={currentPODetail?.unitPrice || 0}
                  formatter={(value) => formatCurrency(value)}
                  parser={(value) => parseCurrency(value) as unknown as 0}
                  step={1000}
                  min={0}
                  addonAfter="VND"
                />
              );
            },
          },
        ]
      : []),
    // {
    //   title: "Thành tiền",
    //   dataIndex: "productUnit",
    //   key: "total",
    //   width: "15%",
    //   render: (productUnit: IProductUnit, record: PurchaseOrderDetailState) => {
    //     const currentProductUnitPrice = getCurrentProductUnitPrice(
    //       record.product,
    //       productUnit.productUnitId,
    //     );
    //     return formatCurrency(currentProductUnitPrice.price * record.quantity);
    //   },
    // },
  ];

  return (
    <div className="card">
      <div className="mb-4 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <div>
            <h2 className="mb-1 text-xl font-semibold">
              Đơn đặt hàng #{currentPurchaseOrder?.purchaseOrderId}
            </h2>
            {currentPurchaseOrder && (
              <Tag
                color={PURCHASE_ORDER_STATUS_COLOR[currentPurchaseOrder.status]}
              >
                {PURCHASE_ORDER_STATUS_NAME[currentPurchaseOrder.status]}
              </Tag>
            )}
          </div>
        </Space>
      </div>
      <Form
        form={form}
        className="mb-6"
        // onFinish={handleFinish}
        layout="vertical"
      >
        <div className="flex items-center justify-between gap-6">
          <Card className="flex-1 self-stretch">
            <Form.Item label="Nhà cung cấp">
              <Input
                value={currentPurchaseOrder?.supplier.supplierName}
                readOnly
              />
            </Form.Item>
            <Form.Item label="Kho nhận hàng">
              <Input
                value={currentPurchaseOrder?.warehouse.warehouseName}
                readOnly
              />
            </Form.Item>
            <Form.Item label="Người tạo">
              <Input value={currentPurchaseOrder?.user.fullName} readOnly />
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
                disabled
                className="w-full"
                format="DD/MM/YYYY"
                placeholder=""
                onChange={(date: Dayjs) => {
                  form.setFieldsValue({
                    expectedDeliveryDate: date.tz().format("YYYY-MM-DD"),
                  });
                }}
              />
            </Form.Item>
            <Form.Item label="Ghi chú" name="note">
              <Input.TextArea
                readOnly={
                  currentPurchaseOrder?.status ===
                    PurchaseOrderStatus.PENDING ||
                  currentPurchaseOrder?.status === PurchaseOrderStatus.CANCELLED
                }
                rows={2}
              />
            </Form.Item>
          </Card>
          {(currentPurchaseOrder?.status === PurchaseOrderStatus.APPROVED ||
            currentPurchaseOrder?.status === PurchaseOrderStatus.COMPLETED) && (
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
                      value={currentPurchaseOrder?.discountType}
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
                      // onChange={(value) => {
                      //   setDiscountType(value);
                      // }}
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
          )}
        </div>
        <Table
          className="mt-6"
          rowKey={(detail: IPurchaseOrderDetail) => detail.product?.productId}
          dataSource={currentPurchaseOrder?.purchaseOrderDetails || []}
          pagination={false}
          columns={columns}
          bordered={false}
          size="middle"
          rowClassName={(_, index) =>
            index % 2 === 0 ? "table-row-light" : "table-row-gray"
          }
          rowHoverable={false}
        />
      </Form>
    </div>
  );
};

export default ViewPurchaseOrder;
