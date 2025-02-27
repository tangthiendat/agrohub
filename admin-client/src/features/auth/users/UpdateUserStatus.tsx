import { useMutation, useQueryClient } from "@tanstack/react-query";
import { IUser } from "../../../interfaces";
import { userService } from "../../../services";
import { Switch, Tooltip } from "antd";
import toast from "react-hot-toast";

interface UpdateUserStatusProps {
  user: IUser;
}

interface UpdateUserStatusArgs {
  userId: string;
  active: boolean;
}

const UpdateUserStatus: React.FC<UpdateUserStatusProps> = ({ user }) => {
  const queryClient = useQueryClient();
  const { mutate: updateUserStatus, isPending: isUpdating } = useMutation({
    mutationFn: ({ userId, active }: UpdateUserStatusArgs) =>
      userService.updateStatus(userId, active),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["users"],
      });
    },
  });

  return (
    <Tooltip title={user.active ? "Hủy kích hoạt" : "Kích hoạt"}>
      <Switch
        size="small"
        loading={isUpdating}
        checked={user.active}
        onChange={(checked) => {
          updateUserStatus(
            { userId: user.userId, active: checked },
            {
              onSuccess: () => {
                if (checked) {
                  toast.success("Kích hoạt người dùng thành công");
                } else {
                  toast.success("Huỷ kích hoạt người dùng thành công");
                }
              },
              onError: () => {
                if (checked) {
                  toast.success("Kích hoạt người dùng thất bại");
                } else {
                  toast.success("Huỷ kích hoạt người dùng thất bại");
                }
              },
            },
          );
        }}
      />
    </Tooltip>
  );
};

export default UpdateUserStatus;
