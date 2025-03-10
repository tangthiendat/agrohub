import { Switch, Tooltip } from "antd";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { IRole } from "../../../interfaces";
import { roleService } from "../../../services/auth/role-service";

interface UpdateRoleStatusProps {
  role: IRole;
}

interface UpdateRoleStatusArgs {
  roleId: number;
  active: boolean;
}

const UpdateRoleStatus: React.FC<UpdateRoleStatusProps> = ({ role }) => {
  const queryClient = useQueryClient();
  const { mutate: updateRoleStatus, isPending: isUpdating } = useMutation({
    mutationFn: ({ roleId, active }: UpdateRoleStatusArgs) =>
      roleService.updateStatus(roleId, active),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["roles"],
      });
    },
  });

  return (
    <Tooltip title={role.active ? "Hủy kích hoạt" : "Kích hoạt"}>
      <Switch
        size="small"
        loading={isUpdating}
        checked={role.active}
        onChange={(checked) => {
          updateRoleStatus(
            { roleId: role.roleId, active: checked },
            {
              onSuccess: () => {
                if (checked) {
                  toast.success("Kích hoạt vai trò thành công");
                } else {
                  toast.success("Huỷ kích hoạt vai trò thành công");
                }
              },
              onError: () => {
                if (checked) {
                  toast.success("Kích hoạt vai trò thất bại");
                } else {
                  toast.success("Huỷ kích hoạt vai trò thất bại");
                }
              },
            },
          );
        }}
      />
    </Tooltip>
  );
};

export default UpdateRoleStatus;
