import { DeleteOutlined } from "@ant-design/icons";
import { Tooltip } from "antd";

interface Props {
  onClick?: () => void;
  tooltipTitle?: string;
}

const DeleteIcon: React.FC<Props> = ({ onClick, tooltipTitle }) => {
  return (
    <Tooltip title={tooltipTitle}>
      <DeleteOutlined
        className="cursor-pointer border border-[#F44336] bg-[#FFEBEE] p-[3px] text-center text-lg text-[#F44336] hover:brightness-95"
        onClick={onClick}
      />
    </Tooltip>
  );
};

export default DeleteIcon;
