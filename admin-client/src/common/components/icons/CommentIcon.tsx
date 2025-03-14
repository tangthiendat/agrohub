import { Tooltip } from "antd";
import { MdOutlineComment } from "react-icons/md";

interface CommentIconProps {
  onClick?: () => void;
  tooltipTitle?: string;
}

const CommentIcon: React.FC<CommentIconProps> = ({ onClick, tooltipTitle }) => {
  return (
    <Tooltip title={tooltipTitle}>
      <MdOutlineComment
        className="cursor-pointer border border-[#2196F3] bg-[#E3F2FD] p-[3px] text-center text-lg text-[#2196F3] hover:brightness-95"
        onClick={onClick}
      />
    </Tooltip>
  );
};

export default CommentIcon;
