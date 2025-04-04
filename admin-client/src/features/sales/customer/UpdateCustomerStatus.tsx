import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Switch, Tooltip } from "antd";
import toast from "react-hot-toast";
import { ICustomer } from "../../interfaces";
import { customerService } from "../../services";

interface UpdateCustomerStatusProps {
  customer: ICustomer;
}

interface UpdateCustomerStatusArgs {
  customerId: string;
  active: boolean;
}

const UpdateCustomerStatus: React.FC<UpdateCustomerStatusProps> = ({
  customer,
}) => {
  const queryClient = useQueryClient();
  const { mutate: updateCustomerStatus, isPending: isUpdating } = useMutation({
    mutationFn: ({ customerId, active }: UpdateCustomerStatusArgs) =>
      customerService.updateStatus(customerId, active),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["customers"],
      });
    },
  });
  return (
    <Tooltip title={customer.active ? "Hủy kích hoạt" : "Kích hoạt"}>
      <Switch
        size="small"
        loading={isUpdating}
        checked={customer.active}
        onChange={(checked) => {
          updateCustomerStatus(
            { customerId: customer.customerId, active: checked },
            {
              onSuccess: () => {
                if (checked) {
                  toast.success("Kích hoạt khách hàng thành công");
                } else {
                  toast.success("Huỷ kích hoạt khách hàng thành công");
                }
              },
              onError: () => {
                if (checked) {
                  toast.error("Kích hoạt khách hàng thất bại");
                } else {
                  toast.error("Huỷ kích hoạt khách hàng thất bại");
                }
              },
            },
          );
        }}
      />
    </Tooltip>
  );
};

export default UpdateCustomerStatus;
