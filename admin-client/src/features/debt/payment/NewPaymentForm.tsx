import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
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
import { useEffect } from "react";
import toast from "react-hot-toast";
import { useShallow } from "zustand/react/shallow";
import { useCurrentUserInfo, useCurrentWarehouse } from "../../../common/hooks";
import { CreatePaymentRequest, ISupplier } from "../../../interfaces";
import { paymentService } from "../../../services";
import { debtAccountService, paymentMethodService } from "../../../services";
import { formatCurrency, parseCurrency } from "../../../utils/number";
import { usePaymentStore } from "../../../store/payment-store";
import { getNotificationMessage } from "../../../utils/notification";
import PaymentDetailTable from "./PaymentDetailTable";

interface NewPaymentFormProps {
  supplier: ISupplier;
  onCancel: () => void;
}

const NewPaymentForm: React.FC<NewPaymentFormProps> = ({
  supplier,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const queryClient = useQueryClient();
  const {
    createdDate,
    totalPaidAmount,
    paymentMethodId,
    paymentDetails,
    setWarehouse,
    setUser,
    initPaymentDetails,
    reset,
    setTotalPaidAmount,
    setPaymentMethodId,
  } = usePaymentStore(
    useShallow((state) => ({
      createdDate: state.createdDate,
      totalPaidAmount: state.totalPaidAmount,
      paymentMethodId: state.paymentMethodId,
      paymentDetails: state.paymentDetails,
      setWarehouse: state.setWarehouse,
      setUser: state.setUser,
      initPaymentDetails: state.initPaymentDetails,
      reset: state.reset,
      setTotalPaidAmount: state.setTotalPaidAmount,
      setPaymentMethodId: state.setPaymentMethodId,
    })),
  );

  const { currentWarehouse, isLoading: isWarehouseLoading } =
    useCurrentWarehouse();

  const { currentUserInfo, isLoading: isUserLoading } = useCurrentUserInfo();

  const { data: partyDebtAccounts, isLoading: isDebtAccountLoading } = useQuery(
    {
      queryKey: ["debt-accounts", "party", supplier.supplierId, "unpaid"],
      queryFn: () =>
        debtAccountService.getUnpaidPartyDebtAccount(supplier.supplierId),
      enabled: !!supplier.supplierId,
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

  const { mutate: createPayment, isPending: isCreating } = useMutation({
    mutationFn: paymentService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) =>
          query.queryKey.includes("debt-accounts") ||
          query.queryKey.includes("payments"),
      });

      onCancel();
    },
  });

  useEffect(() => {
    if (currentWarehouse) {
      setWarehouse(currentWarehouse);
    }
    if (currentUserInfo) {
      setUser(currentUserInfo);
    }
    if (partyDebtAccounts) {
      initPaymentDetails(partyDebtAccounts);
    }
  }, [
    currentWarehouse,
    currentUserInfo,
    partyDebtAccounts,
    setWarehouse,
    setUser,
    initPaymentDetails,
  ]);

  const totalDebt = partyDebtAccounts?.reduce<number>(
    (acc, cur) => acc + cur.remainingAmount,
    0,
  );

  function handleFinish() {
    const newPayment: CreatePaymentRequest = {
      supplierId: supplier.supplierId,
      warehouseId: currentWarehouse!.warehouseId,
      userId: currentUserInfo!.userId,
      createdDate: createdDate!,
      totalPaidAmount: totalPaidAmount!,
      paymentMethodId: paymentMethodId!,
      paymentDetails: paymentDetails.map((detail) => ({
        debtAccountId: detail.debtAccount.debtAccountId,
        paymentAmount: detail.paymentAmount,
      })),
    };
    createPayment(newPayment, {
      onSuccess: () => {
        toast.success("Lập phiếu chi thành công");
        reset();
        form.resetFields();
      },
      onError: (error) => {
        toast.error(getNotificationMessage(error) || "Lập phiếu chi thất bại");
      },
    });
  }

  return (
    <Skeleton
      loading={
        isWarehouseLoading ||
        isUserLoading ||
        isDebtAccountLoading ||
        isPaymentMethodLoading
      }
    >
      <Form
        form={form}
        layout="vertical"
        // labelCol={{ span: 6 }}
        // wrapperCol={{ span: 18 }}
        onFinish={handleFinish}
        initialValues={{
          createdDate: dayjs().tz().format("YYYY-MM-DD"),
          totalPaidAmount: 0,
        }}
      >
        <div className="flex items-center justify-between gap-10">
          <div className="flex-1 self-stretch">
            <Form.Item label="Nhà cung cấp">
              <Input value={supplier.supplierName} readOnly />
            </Form.Item>
            <Form.Item label="Kho nhận hàng">
              <Input value={currentWarehouse?.warehouseName} readOnly />
            </Form.Item>
            <Form.Item label="Người tạo">
              <Input value={currentUserInfo?.fullName} readOnly />
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
                // addonAfter={<div className="w-[55px]">VND</div>}
              />
            </Form.Item>
            <Form.Item
              label="Số tiền trả"
              name="totalPaidAmount"
              rules={[
                {
                  required: true,
                  message: "Vui lòng nhập số tiền trả",
                },
                {
                  validator: (_, value) => {
                    if (value === 0) {
                      return Promise.reject("Số tiền trả phải lớn hơn 0");
                    } else if (value > totalDebt!) {
                      return Promise.reject(
                        "Số tiền trả không được lớn hơn số nợ cũ",
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
                value={totalPaidAmount}
                onChange={(value) => {
                  form.setFieldsValue({ totalPaidAmount: value });
                  setTotalPaidAmount(value as number);
                }}
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                step={1000}
                min={0}
                // addonAfter={<div className="w-[55px]">VND</div>}
              />
            </Form.Item>
            <Form.Item
              label="Phương thức thanh toán"
              name="paymentMethodId"
              rules={[{ required: true, message: "Vui lòng chọn phương thức" }]}
            >
              <Select
                options={paymentMethodOptions}
                onChange={(value) => {
                  setPaymentMethodId(value as string);
                }}
              />
            </Form.Item>
            <Form.Item label="Nợ sau">
              <InputNumber
                className="right-aligned-number w-full"
                controls={false}
                readOnly
                value={totalDebt! - totalPaidAmount}
                formatter={(value) => formatCurrency(value)}
                parser={(value) => parseCurrency(value) as unknown as 0}
                step={1000}
                min={0}
                // addonAfter={<div className="w-[55px]">VND</div>}
              />
            </Form.Item>
          </div>
          <div className="flex-1 self-stretch">
            <Form.Item label="Ghi chú" name="note">
              <Input.TextArea rows={2} />
            </Form.Item>
          </div>
        </div>
        <PaymentDetailTable />
        <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
          <Space>
            <Button
              onClick={() => {
                onCancel();
                reset();
              }}
              loading={isCreating}
            >
              Hủy
            </Button>
            <Button
              type="primary"
              htmlType="submit"
              loading={isCreating}
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

export default NewPaymentForm;
