import { useQuery } from "@tanstack/react-query";
import {
  Button,
  DatePicker,
  Form,
  Input,
  InputNumber,
  Select,
  Skeleton,
  Space,
} from "antd";
import dayjs, { Dayjs } from "dayjs";
// import ReceiptDetailTable from "./ReceiptDetailTable";
import { useCurrentUserInfo, useCurrentWarehouse } from "../../../common/hooks";
import { debtAccountService, paymentMethodService } from "../../../services";
// import { useReceiptStore } from "../../../store/receipt-store";
import { ICustomer } from "../../../interfaces";
import { formatCurrency, parseCurrency } from "../../../utils/number";
import ReceiptDetailTable from "./ReceiptDetailTable";

interface NewReceiptFormProps {
  customer: ICustomer;
  onCancel: () => void;
}

const NewReceiptForm: React.FC<NewReceiptFormProps> = ({
  customer,
  onCancel,
}) => {
  const [form] = Form.useForm();
  // const queryClient = useQueryClient();
  // const {
  //   createdDate,
  //   totalReceivedAmount,
  //   paymentMethodId,
  //   receiptDetails,
  //   setWarehouse,
  //   setUser,
  //   initReceiptDetails,
  //   reset,
  //   setTotalReceivedAmount,
  //   setPaymentMethodId,
  // } = useReceiptStore(
  //   useShallow((state) => ({
  //     createdDate: state.createdDate,
  //     totalReceivedAmount: state.totalReceivedAmount,
  //     paymentMethodId: state.paymentMethodId,
  //     receiptDetails: state.receiptDetails,
  //     setWarehouse: state.setWarehouse,
  //     setUser: state.setUser,
  //     initReceiptDetails: state.initReceiptDetails,
  //     reset: state.reset,
  //     setTotalReceivedAmount: state.setTotalReceivedAmount,
  //     setPaymentMethodId: state.setPaymentMethodId,
  //   })),
  // );

  // const { currentWarehouse, isLoading: isWarehouseLoading } =
  //   useCurrentWarehouse();

  // const { currentUserInfo, isLoading: isUserLoading } = useCurrentUserInfo();

  const { data: partyDebtAccounts, isLoading: isDebtAccountLoading } = useQuery(
    {
      queryKey: ["debt-accounts", "customer", customer.customerId, "unpaid"],
      queryFn: () =>
        debtAccountService.getUnpaidCustomerDebtAccount(customer.customerId),
      enabled: !!customer.customerId,
      select: (data) => data.payload,
    },
  );

  const { data: paymentMethodOptions, isLoading: isPaymentMethodLoading } =
    useQuery({
      queryKey: ["payment-methods"],
      queryFn: () => paymentMethodService.getAll(),
      select: (data) =>
        data.payload.map((item) => ({
          label: item.paymentMethodName,
          value: item.paymentMethodId,
        })),
    });

  // const { mutate: createReceipt, isPending: isCreating } = useMutation({
  //   mutationFn: (data: any) => Promise.resolve(data), // Replace with actual receipt service
  //   onSuccess: () => {
  //     queryClient.invalidateQueries({
  //       predicate: (query) =>
  //         query.queryKey.includes("debt-accounts") ||
  //         query.queryKey.includes("receipts"),
  //     });
  //     onCancel();
  //   },
  // });

  // useEffect(() => {
  //   if (currentWarehouse) {
  //     setWarehouse(currentWarehouse);
  //   }
  //   if (currentUserInfo) {
  //     setUser(currentUserInfo);
  //   }
  //   if (partyDebtAccounts) {
  //     initReceiptDetails(partyDebtAccounts);
  //   }
  // }, [
  //   currentWarehouse,
  //   currentUserInfo,
  //   partyDebtAccounts,
  //   setWarehouse,
  //   setUser,
  //   initReceiptDetails,
  // ]);

  const totalDebt = partyDebtAccounts?.reduce<number>(
    (acc, cur) => acc + cur.remainingAmount,
    0,
  );

  // function handleFinish() {
  //   const newReceipt = {
  //     customerId: customer.customer_id,
  //     warehouseId: currentWarehouse!.warehouseId,
  //     userId: currentUserInfo!.userId,
  //     createdDate: createdDate!,
  //     totalReceivedAmount: totalReceivedAmount!,
  //     paymentMethodId: paymentMethodId!,
  //     receiptDetails: receiptDetails.map((detail) => ({
  //       debtAccountId: detail.debtAccount.debtAccountId,
  //       receiptAmount: detail.receiptAmount,
  //     })),
  //   };

  //   createReceipt(newReceipt, {
  //     onSuccess: () => {
  //       toast.success("Lập phiếu thu thành công");
  //       reset();
  //       form.resetFields();
  //     },
  //     onError: (error) => {
  //       toast.error(getNotificationMessage(error) || "Lập phiếu thu thất bại");
  //     },
  //   });
  // }

  return (
    <Skeleton
      loading={
        // isWarehouseLoading ||
        // isUserLoading ||
        isDebtAccountLoading || isPaymentMethodLoading
      }
    >
      <Form
        form={form}
        layout="vertical"
        // onFinish={handleFinish}
        initialValues={{
          createdDate: dayjs().tz().format("YYYY-MM-DD"),
          totalReceivedAmount: 0,
        }}
      >
        <div className="flex items-center justify-between gap-10">
          <div className="flex-1 self-stretch">
            <Form.Item label="Khách hàng">
              <Input value={customer.customerName} readOnly />
            </Form.Item>
            <Form.Item label="Kho">
              <Input value={"Kho chính"} readOnly />
            </Form.Item>
            <Form.Item label="Người tạo">
              <Input value={"Tăng Thiện Đạt"} readOnly />
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
          </div>
          <div className="flex-1 self-stretch">
            <Form.Item label="Nợ cũ">
              <InputNumber
                className="right-aligned-number w-full"
                controls={false}
                readOnly
                value={totalDebt}
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                step={1000}
                min={0}
              />
            </Form.Item>
            <Form.Item
              label="Số tiền thu"
              name="totalReceivedAmount"
              rules={[
                {
                  required: true,
                  message: "Vui lòng nhập số tiền thu",
                },
                {
                  validator: (_, value) => {
                    if (value === 0) {
                      return Promise.reject("Số tiền thu phải lớn hơn 0");
                    } else if (value > totalDebt!) {
                      return Promise.reject(
                        "Số tiền thu không được lớn hơn số nợ cũ",
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
                max={totalDebt}
                // value={totalReceivedAmount}
                // onChange={(value) => {
                //   form.setFieldsValue({ totalReceivedAmount: value });
                //   setTotalReceivedAmount(value as number);
                // }}
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                step={1000}
                min={0}
              />
            </Form.Item>
            <Form.Item
              label="Phương thức thanh toán"
              name="paymentMethodId"
              rules={[{ required: true, message: "Vui lòng chọn phương thức" }]}
            >
              <Select
                options={paymentMethodOptions}
                // onChange={(value) => {
                //   setPaymentMethodId(value as string);
                // }}
              />
            </Form.Item>
            <Form.Item label="Nợ sau">
              <InputNumber
                className="right-aligned-number w-full"
                controls={false}
                readOnly
                // value={totalDebt! - totalReceivedAmount}
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                step={1000}
                min={0}
              />
            </Form.Item>
          </div>
          <div className="flex-1 self-stretch">
            <Form.Item label="Ghi chú" name="note">
              <Input.TextArea rows={2} />
            </Form.Item>
          </div>
        </div>
        <ReceiptDetailTable receiptDetails={partyDebtAccounts || []} />
        <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
          <Space>
            <Button
              onClick={() => {
                onCancel();
                // reset();
              }}
              // loading={isCreating}
            >
              Hủy
            </Button>
            <Button
              type="primary"
              htmlType="submit"
              // loading={isCreating}
              disabled={partyDebtAccounts?.length === 0}
            >
              Lưu
            </Button>
          </Space>
        </Form.Item>
      </Form>
    </Skeleton>
  );
};

export default NewReceiptForm;
