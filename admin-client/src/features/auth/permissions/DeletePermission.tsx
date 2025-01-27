import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Popconfirm } from "antd";
import toast from "react-hot-toast";
import DeleteIcon from "../../../common/components/icons/DeleteIcon";
import { permissionService } from "../../../services";
import { getNotificationMessage } from "../../../utils/notification";

interface DeletePermissionProps {
  permissionId: number;
}

const DeletePermission: React.FC<DeletePermissionProps> = ({
  permissionId,
}) => {
  const queryClient = useQueryClient();

  const { mutate: deletePermission, isPending: isDeleting } = useMutation({
    mutationFn: permissionService.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => query.queryKey.includes("permissions"),
      });
    },
  });

  function handleConfirmDelete(): void {
    deletePermission(permissionId, {
      onSuccess: () => {
        toast.success("Xóa quyền hạn thành công");
      },
      onError: (error: Error) => {
        toast.error(
          getNotificationMessage(error) ||
            "Có lỗi xảy ra. Vui lòng thử lại sau.",
        );
      },
    });
  }

  return (
    <Popconfirm
      title="Xóa quyền hạn này?"
      description="Hành động này không thể hoàn tác."
      okText="Xóa"
      cancelText="Hủy"
      okButtonProps={{ danger: true, loading: isDeleting }}
      onConfirm={handleConfirmDelete}
    >
      <DeleteIcon tooltipTitle="Xoá" />
    </Popconfirm>
  );
};

export default DeletePermission;
