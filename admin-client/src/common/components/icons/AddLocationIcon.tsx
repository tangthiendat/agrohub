import { Tooltip } from "antd";
import { MdAddLocationAlt } from "react-icons/md";

interface AddLocationIconProps {
  onClick?: () => void;
  tooltipTitle?: string;
}

const AddLocationIcon: React.FC<AddLocationIconProps> = ({
  tooltipTitle,
  onClick,
}) => {
  return (
    <Tooltip title={tooltipTitle}>
      <MdAddLocationAlt
        className="cursor-pointer text-center text-2xl text-[#2196F3] hover:brightness-95"
        onClick={onClick}
      />
    </Tooltip>
  );
};

export default AddLocationIcon;
