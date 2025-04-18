import React from "react";
import { Card, Typography, Tooltip } from "antd";
import {
  ArrowUpOutlined,
  ArrowDownOutlined,
  ShoppingCartOutlined,
  DollarOutlined,
  ImportOutlined,
  ExportOutlined,
} from "@ant-design/icons";
import { formatCurrency } from "../../utils/number";

const { Title, Text } = Typography;

interface StatsCardProps {
  title: string;
  value: number;
  isCurrency?: boolean;
  changePercentage: number;
  trend: "up" | "down";
  icon: "order" | "money" | "import" | "export";
}

const StatsCard: React.FC<StatsCardProps> = ({
  title,
  value,
  isCurrency = false,
  changePercentage,
  trend,
  icon,
}) => {
  const renderIcon = () => {
    switch (icon) {
      case "order":
        return <ShoppingCartOutlined className="text-3xl text-blue-500" />;
      case "money":
        return <DollarOutlined className="text-3xl text-green-500" />;
      case "import":
        return <ImportOutlined className="text-3xl text-indigo-500" />;
      case "export":
        return <ExportOutlined className="text-3xl text-orange-500" />;
      default:
        return <ShoppingCartOutlined className="text-3xl text-blue-500" />;
    }
  };

  return (
    <Card className="h-full shadow-md">
      <div className="flex items-center justify-between">
        <div>
          <Text className="text-gray-500">{title}</Text>
          <Title level={3} className="m-0 mt-1">
            {isCurrency ? formatCurrency(value) : value.toLocaleString()}
          </Title>
          <Tooltip title={`${changePercentage}% so với tháng trước`}>
            <Text
              className={`mt-2 flex items-center ${
                trend === "up" ? "text-green-500" : "text-red-500"
              }`}
            >
              {trend === "up" ? <ArrowUpOutlined /> : <ArrowDownOutlined />}
              <span className="ml-1">{changePercentage}%</span>
            </Text>
          </Tooltip>
        </div>
        <div className="flex items-center justify-center rounded-full bg-gray-100 p-4">
          {renderIcon()}
        </div>
      </div>
    </Card>
  );
};

export default StatsCard;
