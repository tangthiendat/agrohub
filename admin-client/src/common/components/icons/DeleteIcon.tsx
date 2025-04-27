import { DeleteOutlined } from "@ant-design/icons";
import { Tooltip } from "antd";

interface DeleteIconProps {
  onClick?: () => void;
  tooltipTitle?: string;
}

const DeleteIcon: React.FC<DeleteIconProps> = ({ onClick, tooltipTitle }) => {
  return (
    <Tooltip title={tooltipTitle}>
      <DeleteOutlined
        className="cursor-pointer text-center text-lg text-[#F44336] hover:brightness-95"
        onClick={onClick}
      />
    </Tooltip>
  );
};

export default DeleteIcon;
