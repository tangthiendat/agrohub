import { useMutation, useQueryClient } from "@tanstack/react-query";
import { ISupplier } from "../../interfaces";
import { supplierService } from "../../services";
import { Switch, Tooltip } from "antd";
import toast from "react-hot-toast";

interface UpdateSupplierStatusProps {
  supplier: ISupplier;
}

interface UpdateSupplierStatusArgs {
  supplierId: string;
  active: boolean;
}

const UpdateSupplierStatus: React.FC<UpdateSupplierStatusProps> = ({
  supplier,
}) => {
  const queryClient = useQueryClient();
  const { mutate: updateRoleStatus, isPending: isUpdating } = useMutation({
    mutationFn: ({ supplierId, active }: UpdateSupplierStatusArgs) =>
      supplierService.updateStatus(supplierId, active),
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey.includes("suppliers");
        },
      });
    },
  });
  return (
    <Tooltip title={supplier.active ? "Hủy kích hoạt" : "Kích hoạt"}>
      <Switch
        size="small"
        loading={isUpdating}
        checked={supplier.active}
        onChange={(checked) => {
          updateRoleStatus(
            { supplierId: supplier.supplierId, active: checked },
            {
              onSuccess: () => {
                if (checked) {
                  toast.success("Kích hoạt nhà cung cấp thành công");
                } else {
                  toast.success("Huỷ kích hoạt nhà cung cấp thành công");
                }
              },
              onError: () => {
                if (checked) {
                  toast.success("Kích hoạt nhà cung cấp thất bại");
                } else {
                  toast.success("Huỷ kích hoạt nhà cung cấp thất bại");
                }
              },
            },
          );
        }}
      />
    </Tooltip>
  );
};

export default UpdateSupplierStatus;
