import { EyeOutlined } from "@ant-design/icons";
import { Tooltip } from "antd";

interface Props {
  onClick: () => void;
  tooltipTitle?: string;
}

const ViewDetailsIcon: React.FC<Props> = ({ onClick, tooltipTitle }) => {
  return (
    <Tooltip title={tooltipTitle}>
      <EyeOutlined
        className="cursor-pointer border border-[#2196f3] bg-[#E3F2FD] p-[3px] text-center text-lg text-[#2196F3] hover:brightness-95"
        onClick={onClick}
      />
    </Tooltip>
  );
};

export default ViewDetailsIcon;
