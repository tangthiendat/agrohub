import { Button, Tooltip } from "antd";
import { GoArrowLeft } from "react-icons/go";
import { useNavigate } from "react-router";

const BackButton: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Tooltip title="Quay lại">
      <Button icon={<GoArrowLeft />} onClick={() => navigate(-1)} />
    </Tooltip>
  );
};

export default BackButton;
