import { Tooltip } from "antd";
import { CommentOutlined } from "@ant-design/icons";

interface CommentIconProps {
  onClick?: () => void;
  tooltipTitle?: string;
}

const CommentIcon: React.FC<CommentIconProps> = ({ onClick, tooltipTitle }) => {
  return (
    <Tooltip title={tooltipTitle}>
      <CommentOutlined
        className="cursor-pointer text-center text-lg text-[#2196F3] hover:brightness-95"
        onClick={onClick}
      />
    </Tooltip>
  );
};

export default CommentIcon;
