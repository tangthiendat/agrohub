import { Tooltip } from "antd";
import { EditOutlined } from "@ant-design/icons";

interface EditIconProps {
  onClick: () => void;
  tooltipTitle?: string;
}

const EditIcon: React.FC<EditIconProps> = ({ onClick, tooltipTitle }) => {
  return (
    <Tooltip title={tooltipTitle}>
      <EditOutlined
        className="cursor-pointer p-[3px] text-center text-xl text-[#FF9800] hover:brightness-95"
        onClick={onClick}
      />
    </Tooltip>
  );
};

export default EditIcon;
