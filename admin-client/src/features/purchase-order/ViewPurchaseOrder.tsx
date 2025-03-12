import {
  useMutation,
  useQueries,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import {
  Button,
  Card,
  DatePicker,
  Form,
  Input,
  InputNumber,
  Modal,
  Select,
  Space,
  Table,
  Tag,
  Typography,
} from "antd";
import { TableProps } from "antd/lib";
import dayjs, { Dayjs } from "dayjs";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
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
  UpdatePurchaseOrderRequest,
} from "../../interfaces";
import { productService, purchaseOrderService } from "../../services";
import { getFinalAmount } from "../../utils/data";
import { getNotificationMessage } from "../../utils/notification";
import { formatCurrency, parseCurrency } from "../../utils/number";

interface UpdatePurchaseOrderStatusArgs {
  purchaseOrderId: string;
  status: PurchaseOrderStatus;
}

interface UpdatePurchaseOrderArgs {
  purchaseOrderId: string;
  purchaseOrder: UpdatePurchaseOrderRequest;
}

interface CancelPurchaseOrderArgs {
  purchaseOrderId: string;
  cancelReason: string;
}

const ViewPurchaseOrder: React.FC = () => {
  const { id } = useParams();
  const [currentPurchaseOrder, setCurrentPurchaseOrder] = useState<
    IPurchaseOrder | undefined
  >(undefined);
  const [form] = Form.useForm<IPurchaseOrder>();
  const [cancelPurchaseOrderForm] = Form.useForm();
  const queryClient = useQueryClient();
  const [isOpenCancelModal, setIsOpenCancelModal] = useState<boolean>(false);

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

  const { mutate: updatePurchaseOrderStatus, isPending: isUpdatingStatus } =
    useMutation({
      mutationFn: ({
        purchaseOrderId,
        status,
      }: UpdatePurchaseOrderStatusArgs) =>
        purchaseOrderService.updateStatus(purchaseOrderId, status),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["purchase-orders"],
        });
      },
    });

  const { mutate: updatePurchaseOrder, isPending: isUpdating } = useMutation({
    mutationFn: ({ purchaseOrderId, purchaseOrder }: UpdatePurchaseOrderArgs) =>
      purchaseOrderService.update(purchaseOrderId, purchaseOrder),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["purchase-orders"],
      });
    },
  });

  const { mutate: cancelPurchaseOrder, isPending: isCanceling } = useMutation({
    mutationFn: ({ purchaseOrderId, cancelReason }: CancelPurchaseOrderArgs) =>
      purchaseOrderService.cancel(purchaseOrderId, cancelReason),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["purchase-orders"],
      });
    },
  });

  const handleOpenCancelModal = () => {
    setIsOpenCancelModal(true);
  };

  const handleCloseCancelModal = () => {
    setIsOpenCancelModal(false);
  };

  useEffect(() => {
    if (purchaseOrder && productsMap) {
      const fullPurchaseOrder = {
        ...purchaseOrder,

        purchaseOrderDetails: purchaseOrder.purchaseOrderDetails.map((pod) => ({
          ...pod,
          product: productsMap[pod.product.productId],
          unitPrice: pod.unitPrice || 0,
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
      width: "10%",
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
      width: "10%",
      render: (quantity: number) => (
        <InputNumber
          readOnly={
            currentPurchaseOrder?.status === PurchaseOrderStatus.COMPLETED
          }
          value={quantity}
          min={1}
        />
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
                  className="right-aligned-number w-full"
                  controls={false}
                  readOnly={
                    currentPurchaseOrder?.status ===
                    PurchaseOrderStatus.COMPLETED
                  }
                  value={currentPODetail?.unitPrice || 0}
                  formatter={(value) => formatCurrency(value)}
                  parser={(value) => parseCurrency(value) as unknown as 0}
                  step={1000}
                  min={0}
                  addonAfter="VND"
                  onChange={(value) => {
                    const newPODetails =
                      currentPurchaseOrder?.purchaseOrderDetails.map((pod) => {
                        if (
                          pod.product?.productId === record.product?.productId
                        ) {
                          return {
                            ...pod,
                            unitPrice: value as number,
                          };
                        }
                        return pod;
                      });
                    const totalAmount = newPODetails?.reduce(
                      (acc, pod) => acc + (pod.unitPrice || 0) * pod.quantity,
                      0,
                    );
                    const vatRate = currentPurchaseOrder?.vatRate || 0;
                    const discountValue =
                      currentPurchaseOrder?.discountValue || 0;
                    const discountType = currentPurchaseOrder?.discountType;
                    const finalAmount = getFinalAmount(
                      totalAmount,
                      discountValue,
                      discountType,
                      vatRate,
                    );
                    form.setFieldsValue({
                      totalAmount: totalAmount,
                      purchaseOrderDetails: newPODetails,
                      finalAmount: finalAmount,
                    });
                    setCurrentPurchaseOrder({
                      ...currentPurchaseOrder!,
                      purchaseOrderDetails: newPODetails || [],
                      totalAmount: totalAmount || 0,
                      finalAmount: finalAmount,
                    });
                  }}
                />
              );
            },
          },
        ]
      : []),
    ...(currentPurchaseOrder?.status === PurchaseOrderStatus.APPROVED ||
    currentPurchaseOrder?.status === PurchaseOrderStatus.COMPLETED
      ? [
          {
            title: "Thành tiền",
            dataIndex: "productUnit",
            key: "total",
            width: "15%",
            render: (_: IProductUnit, record: IPurchaseOrderDetail) => {
              return (
                <InputNumber
                  value={(record.unitPrice || 0) * record.quantity}
                  className="right-aligned-number w-full"
                  controls={false}
                  readOnly
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
  ];

  function handleFinish() {
    const updatedPurchaseOrder: UpdatePurchaseOrderRequest = {
      purchaseOrderId: currentPurchaseOrder!.purchaseOrderId,
      userId: currentPurchaseOrder!.user.userId,
      warehouseId: currentPurchaseOrder!.warehouse.warehouseId,
      supplierId: currentPurchaseOrder!.supplier.supplierId,
      status: currentPurchaseOrder!.status,
      orderDate: currentPurchaseOrder!.orderDate,
      expectedDeliveryDate: currentPurchaseOrder!.expectedDeliveryDate,
      totalAmount: currentPurchaseOrder!.totalAmount,
      discountValue: currentPurchaseOrder!.discountValue,
      discountType: currentPurchaseOrder!.discountType,
      vatRate: currentPurchaseOrder!.vatRate,
      finalAmount: currentPurchaseOrder!.finalAmount,
      purchaseOrderDetails: currentPurchaseOrder!.purchaseOrderDetails.map(
        (pod) => ({
          productId: pod.product.productId,
          productUnitId: pod.productUnit.productUnitId,
          quantity: pod.quantity,
          unitPrice: pod.unitPrice,
        }),
      ),
      note: form.getFieldValue("note"),
    };
    updatePurchaseOrder(
      {
        purchaseOrderId: currentPurchaseOrder!.purchaseOrderId,
        purchaseOrder: updatedPurchaseOrder,
      },
      {
        onSuccess: () => {
          toast.success("Cập nhật đơn đặt hàng thành công");
        },
        onError: (error: Error) => {
          toast.error(
            getNotificationMessage(error) || "Cập nhật đơn đặt hàng thất bại",
          );
        },
      },
    );
  }

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
        <div className="mt-2 flex space-x-2">
          {purchaseOrder?.status !== PurchaseOrderStatus.COMPLETED &&
            purchaseOrder?.status !== PurchaseOrderStatus.CANCELLED && (
              <>
                <Button danger onClick={handleOpenCancelModal}>
                  Hủy
                </Button>
                <Modal
                  open={isOpenCancelModal}
                  width="30%"
                  title={<span className="text-lg">Hủy đơn đặt hàng</span>}
                  destroyOnClose
                  onCancel={handleCloseCancelModal}
                  cancelText="Đóng"
                  okText="Hủy đơn hàng"
                  onOk={() => cancelPurchaseOrderForm.submit()}
                  cancelButtonProps={{
                    disabled: isCanceling,
                  }}
                  okButtonProps={{
                    disabled: isCanceling,
                  }}
                >
                  <Form
                    form={cancelPurchaseOrderForm}
                    layout="vertical"
                    onFinish={(values) => {
                      cancelPurchaseOrder(
                        {
                          purchaseOrderId:
                            currentPurchaseOrder!.purchaseOrderId,
                          cancelReason: values.cancelReason as string,
                        },
                        {
                          onSuccess: () => {
                            toast.success("Hủy đơn hàng thành công");
                            cancelPurchaseOrderForm.resetFields();
                            handleCloseCancelModal();
                          },
                          onError: (error: Error) => {
                            toast.error(
                              getNotificationMessage(error) ||
                                "Hủy đơn hàng thất bại",
                            );
                          },
                        },
                      );
                    }}
                  >
                    <Form.Item
                      label="Lý do hủy"
                      name="cancelReason"
                      rules={[
                        {
                          required: true,
                          message: "Vui lòng nhập lý do hủy đơn hàng",
                        },
                      ]}
                    >
                      <Input.TextArea rows={3} />
                    </Form.Item>
                  </Form>
                </Modal>
              </>
            )}

          {purchaseOrder?.status === PurchaseOrderStatus.PENDING && (
            <Button
              type="primary"
              disabled={isUpdatingStatus || isUpdating}
              onClick={() => {
                updatePurchaseOrderStatus(
                  {
                    purchaseOrderId: currentPurchaseOrder!.purchaseOrderId,
                    status: PurchaseOrderStatus.APPROVED,
                  },
                  {
                    onSuccess: () => {
                      toast.success("Đã xác nhận đơn hàng");
                    },
                    onError: (error: Error) => {
                      toast.error(
                        getNotificationMessage(error) ||
                          "Xác nhận đơn hàng thất bại",
                      );
                    },
                  },
                );
              }}
            >
              Xác nhận
            </Button>
          )}
          {purchaseOrder?.status === PurchaseOrderStatus.APPROVED && (
            <Button
              type="primary"
              onClick={() => {
                form.submit();
              }}
            >
              Cập nhật
            </Button>
          )}
          {purchaseOrder?.status === PurchaseOrderStatus.APPROVED && (
            <Button
              type="primary"
              disabled={isUpdatingStatus || isUpdating}
              onClick={() => {
                updatePurchaseOrderStatus(
                  {
                    purchaseOrderId: currentPurchaseOrder!.purchaseOrderId,
                    status: PurchaseOrderStatus.COMPLETED,
                  },
                  {
                    onSuccess: () => {
                      toast.success("Đã hoàn thành đơn hàng");
                    },
                    onError: (error: Error) => {
                      toast.error(
                        getNotificationMessage(error) ||
                          "Hoàn thành đơn hàng thất bại",
                      );
                    },
                  },
                );
              }}
            >
              Hoàn thành
            </Button>
          )}
        </div>
      </div>
      <Form
        form={form}
        className="mb-6"
        onFinish={handleFinish}
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
                  currentPurchaseOrder?.status ===
                    PurchaseOrderStatus.CANCELLED ||
                  currentPurchaseOrder?.status === PurchaseOrderStatus.COMPLETED
                }
                rows={2}
              />
            </Form.Item>
          </Card>
          {currentPurchaseOrder?.status === PurchaseOrderStatus.CANCELLED && (
            <Card className="flex-1 self-stretch">
              <Form.Item label="Lý do hủy">
                <Input.TextArea
                  value={currentPurchaseOrder?.cancelReason}
                  readOnly
                  rows={2}
                />
              </Form.Item>
            </Card>
          )}
          {(currentPurchaseOrder?.status === PurchaseOrderStatus.APPROVED ||
            currentPurchaseOrder?.status === PurchaseOrderStatus.COMPLETED) && (
            <Card className="flex-1 self-stretch">
              <Form.Item label="Tổng tiền hàng" name="totalAmount">
                <InputNumber
                  className="right-aligned-number w-full"
                  controls={false}
                  readOnly
                  formatter={(value) => formatCurrency(value)}
                  parser={(value) => parseCurrency(value) as unknown as 0}
                  step={1000}
                  min={0}
                  addonAfter={<div className="w-[55px]">VND</div>}
                />
              </Form.Item>
              <Form.Item
                label="Chiết khấu"
                name="discountValue"
                rules={[
                  {
                    validator: (_, value) => {
                      if (
                        currentPurchaseOrder?.discountType ===
                          DiscountType.AMOUNT &&
                        value > currentPurchaseOrder?.totalAmount
                      ) {
                        return Promise.reject(
                          new Error(
                            "Chiết khấu không được lớn hơn tổng tiền hàng",
                          ),
                        );
                      }
                      if (
                        currentPurchaseOrder?.discountType ===
                          DiscountType.PERCENTAGE &&
                        value > 100
                      ) {
                        return Promise.reject(
                          new Error("Chiết khấu không được lớn hơn 100%"),
                        );
                      }
                      return Promise.resolve();
                    },
                  },
                ]}
              >
                <InputNumber
                  className="right-aligned-number w-full"
                  controls={false}
                  readOnly={
                    currentPurchaseOrder?.status ===
                    PurchaseOrderStatus.COMPLETED
                  }
                  value={currentPurchaseOrder?.discountValue}
                  formatter={(value) => formatCurrency(value)}
                  parser={(value) => parseCurrency(value) as unknown as 0}
                  step={
                    currentPurchaseOrder?.discountType ===
                    DiscountType.PERCENTAGE
                      ? 1
                      : 1000
                  }
                  min={0}
                  addonAfter={
                    <Select
                      disabled={
                        currentPurchaseOrder?.status ===
                        PurchaseOrderStatus.COMPLETED
                      }
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
                          value: DiscountType.PERCENTAGE,
                          label: "%",
                        },
                      ]}
                      onChange={(value) => {
                        const totalAmount =
                          currentPurchaseOrder?.totalAmount || 0;

                        const vatRate = currentPurchaseOrder?.vatRate || 0;
                        const finalAmount = getFinalAmount(
                          totalAmount,
                          0,
                          value,
                          vatRate,
                        );
                        form.setFieldsValue({
                          discountValue: 0,
                          finalAmount: finalAmount,
                        });
                        setCurrentPurchaseOrder({
                          ...currentPurchaseOrder!,
                          discountValue: 0,
                          discountType: value,
                          finalAmount: finalAmount,
                        });
                      }}
                    />
                  }
                  onChange={(value) => {
                    const totalAmount = currentPurchaseOrder?.totalAmount || 0;
                    const discountValue = value || 0;
                    const discountType = currentPurchaseOrder?.discountType;
                    const vatRate = currentPurchaseOrder?.vatRate || 0;
                    const finalAmount = getFinalAmount(
                      totalAmount,
                      discountValue,
                      discountType,
                      vatRate,
                    );
                    form.setFieldsValue({
                      discountValue,
                      finalAmount: finalAmount,
                    });
                    setCurrentPurchaseOrder({
                      ...currentPurchaseOrder!,
                      discountValue,
                      finalAmount: finalAmount,
                    });
                  }}
                />
              </Form.Item>
              <Form.Item
                label="VAT"
                name="vatRate"
                rules={[
                  {
                    validator: (_, value) => {
                      if (value > 100) {
                        return Promise.reject(
                          new Error("VAT không được lớn hơn 100%"),
                        );
                      }
                      return Promise.resolve();
                    },
                  },
                ]}
              >
                <InputNumber
                  className="right-aligned-number w-full"
                  controls={false}
                  readOnly={
                    currentPurchaseOrder.status ===
                    PurchaseOrderStatus.COMPLETED
                  }
                  value={currentPurchaseOrder?.vatRate}
                  formatter={(value) => formatCurrency(value)}
                  parser={(value) => parseCurrency(value) as unknown as 0}
                  min={0}
                  onChange={(value) => {
                    const totalAmount = currentPurchaseOrder?.totalAmount || 0;
                    const discountValue =
                      currentPurchaseOrder?.discountValue || 0;
                    const discountType = currentPurchaseOrder?.discountType;

                    const finalAmount = getFinalAmount(
                      totalAmount,
                      discountValue,
                      discountType,
                      value || 0,
                    );
                    form.setFieldsValue({
                      vatRate: value || 0,
                      finalAmount: finalAmount,
                    });
                    setCurrentPurchaseOrder({
                      ...currentPurchaseOrder,
                      vatRate: value || 0,
                      finalAmount: finalAmount,
                    });
                  }}
                  addonAfter={<div className="w-[55px]">%</div>}
                />
              </Form.Item>
              <Form.Item label="Tổng cộng" name="finalAmount">
                <InputNumber
                  className="right-aligned-number w-full"
                  controls={false}
                  readOnly
                  value={currentPurchaseOrder?.finalAmount}
                  formatter={(value) => formatCurrency(value)}
                  parser={(value) => parseCurrency(value) as unknown as 0}
                  min={0}
                  addonAfter={<div className="w-[55px]">VND</div>}
                />
              </Form.Item>
            </Card>
          )}
        </div>
        <Typography.Title level={5} className="mt-6">
          Chi tiết đơn hàng
        </Typography.Title>
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
