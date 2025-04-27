import { Tooltip } from "antd";
import { FaMoneyCheckDollar } from "react-icons/fa6";

interface MoneyIconProps {
  onClick?: () => void;
  tooltipTitle?: string;
}

const MoneyIcon: React.FC<MoneyIconProps> = ({ tooltipTitle, onClick }) => {
  return (
    <Tooltip title={tooltipTitle}>
      <FaMoneyCheckDollar
        className="cursor-pointer text-center text-lg text-[#4CAF50] hover:brightness-95"
        onClick={onClick}
      />
    </Tooltip>
  );
};

export default MoneyIcon;
