import { Tooltip } from "antd";
import { EditOutlined } from "@ant-design/icons";

interface Props {
  onClick: () => void;
  tooltipTitle?: string;
}

const EditIcon: React.FC<Props> = ({ onClick, tooltipTitle }) => {
  return (
    <Tooltip title={tooltipTitle}>
      <EditOutlined
        className="cursor-pointer border border-[#FF9800] bg-[#FFF3E0] p-[3px] text-center text-lg text-[#FF9800] hover:brightness-95"
        onClick={onClick}
      />
    </Tooltip>
  );
};

export default EditIcon;
