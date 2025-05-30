import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Button, Card, List, Tag } from "antd";
import dayjs from "dayjs";
import toast from "react-hot-toast";
import Loading from "../../../common/components/Loading";
import { PURCHASE_ORDER_STATUS_COLOR } from "../../../common/constants";
import { PURCHASE_ORDER_STATUS_NAME } from "../../../common/constants/status";
import { PurchaseOrderStatus } from "../../../common/enums";
import { PurchaseOrderFilterCriteria } from "../../../interfaces";
import { purchaseOrderService } from "../../../services";
import { getNotificationMessage } from "../../../utils/notification";

interface UpdatePurchaseOrderStatusArgs {
  purchaseOrderId: string;
  status: PurchaseOrderStatus;
}

const PendingOrderList: React.FC = () => {
  const filter: PurchaseOrderFilterCriteria = {
    status: "PENDING",
  };
  const queryClient = useQueryClient();

  const { mutate: updatePurchaseOrderStatus, isPending: isUpdating } =
    useMutation({
      mutationFn: ({
        purchaseOrderId,
        status,
      }: UpdatePurchaseOrderStatusArgs) =>
        purchaseOrderService.updateStatus(purchaseOrderId, status),
    });

  const { data: pendingPurchaseOrders, isLoading } = useQuery({
    queryKey: ["purchase-orders", filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => purchaseOrderService.get(filter),
    select: (data) => data.payload,
  });

  if (isLoading) {
    return <Loading />;
  }

  return (
    <List
      pagination={{
        align: "end",
        showTotal: (total) => `Tổng số: ${total} đơn hàng`,
      }}
      grid={{ gutter: 16, column: 2 }}
      dataSource={pendingPurchaseOrders}
      renderItem={(purchaseOrder) => (
        <List.Item className="mb-4">
          <Card
            className="cursor-pointer border border-gray-200 shadow-md transition-shadow duration-300 hover:shadow-lg"
            bordered={false}
          >
            <div className="space-y-3 p-4">
              <div className="flex items-start justify-between">
                <div className="space-y-2">
                  <div className="flex items-center space-x-2">
                    <span className="text-lg font-bold text-gray-700">
                      Mã đơn hàng:
                    </span>
                    <span className="text-base text-blue-600">
                      {purchaseOrder.purchaseOrderId}
                    </span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <span className="font-semibold text-gray-700">
                      Nhà cung cấp:
                    </span>
                    <span>{purchaseOrder.supplierName}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <span className="font-semibold text-gray-700">
                      Ngày đặt:
                    </span>
                    <span>
                      {dayjs(purchaseOrder.orderDate).tz().format("DD/MM/YYYY")}
                    </span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <span className="font-semibold text-gray-700">
                      Dự kiến giao:
                    </span>
                    <span>
                      {dayjs(purchaseOrder.expectedDeliveryDate)
                        .tz()
                        .format("DD/MM/YYYY")}
                    </span>
                  </div>
                </div>
                <Tag
                  color={PURCHASE_ORDER_STATUS_COLOR[purchaseOrder.status]}
                  className="px-3 py-1 text-sm font-medium"
                  aria-label={`Trạng thái đơn hàng: ${purchaseOrder.status}`}
                >
                  {PURCHASE_ORDER_STATUS_NAME[purchaseOrder.status]}
                </Tag>
              </div>

              {/* {purchaseOrder.note && (
                <div className="mt-2 border-t pt-2">
                  <div className="flex items-start space-x-2">
                    <span className="font-semibold text-gray-700">
                      Ghi chú:
                    </span>
                    <span className="italic text-gray-600">
                      {purchaseOrder.note}
                    </span>
                  </div>
                </div>
              )} */}

              <div className="mt-2 flex space-x-2">
                <Button
                  size="small"
                  danger
                  disabled={isUpdating}
                  onClick={() => {
                    updatePurchaseOrderStatus(
                      {
                        purchaseOrderId: purchaseOrder.purchaseOrderId,
                        status: PurchaseOrderStatus.CANCELLED,
                      },
                      {
                        onSuccess: () => {
                          queryClient.invalidateQueries({
                            queryKey: ["purchase-orders"],
                          });
                          toast.success("Đã hủy đơn hàng");
                        },
                        onError: (error: Error) => {
                          toast.error(
                            getNotificationMessage(error) ||
                              "Hủy đơn hàng thất bại",
                          );
                        },
                      },
                    );
                  }}
                >
                  Hủy
                </Button>

                <Button
                  size="small"
                  type="primary"
                  disabled={isUpdating}
                  onClick={() => {
                    updatePurchaseOrderStatus(
                      {
                        purchaseOrderId: purchaseOrder.purchaseOrderId,
                        status: PurchaseOrderStatus.APPROVED,
                      },
                      {
                        onSuccess: () => {
                          queryClient.invalidateQueries({
                            queryKey: ["purchase-orders"],
                          });
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
              </div>
            </div>
          </Card>
        </List.Item>
      )}
    />
  );
};

export default PendingOrderList;
