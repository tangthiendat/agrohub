// Mock data for dashboard

import { TrendType } from "../../../common/enums";

// Stats for cards
export const dashboardOrderStats = {
  value: 124,
  changePercentage: 12.5,
  trend: TrendType.UP,
};

export const dashboardCustomerDebtStats = {
  value: 78500000,
  changePercentage: 8.3,
  trend: TrendType.DOWN,
};

export const dashboardImportStats = {
  value: 356,
  changePercentage: 5.2,
  trend: TrendType.UP,
};

export const dashboardExportStats = {
  value: 287,
  changePercentage: 3.7,
  trend: TrendType.DOWN,
};

// Inventory activity chart data (imports and exports by date) - Focused on March
// Imports occur on specific days (major inventory restocking), while exports happen more frequently
export const dashboardInventoryActivity = [
  { date: "01/03", imports: 120, exports: 25 },
  { date: "02/03", imports: 0, exports: 32 },
  { date: "03/03", imports: 0, exports: 28 },
  { date: "04/03", imports: 0, exports: 35 },
  { date: "05/03", imports: 85, exports: 30 },
  { date: "06/03", imports: 0, exports: 25 },
  { date: "07/03", imports: 0, exports: 20 },
  { date: "08/03", imports: 0, exports: 23 },
  { date: "09/03", imports: 0, exports: 28 },
  { date: "10/03", imports: 105, exports: 32 },
  { date: "11/03", imports: 0, exports: 36 },
  { date: "12/03", imports: 0, exports: 42 },
  { date: "13/03", imports: 0, exports: 38 },
  { date: "14/03", imports: 0, exports: 29 },
  { date: "15/03", imports: 95, exports: 35 },
  { date: "16/03", imports: 0, exports: 40 },
  { date: "17/03", imports: 0, exports: 45 },
  { date: "18/03", imports: 0, exports: 38 },
  { date: "19/03", imports: 0, exports: 42 },
  { date: "20/03", imports: 130, exports: 35 },
  { date: "21/03", imports: 0, exports: 40 },
  { date: "22/03", imports: 0, exports: 45 },
  { date: "23/03", imports: 0, exports: 38 },
  { date: "24/03", imports: 0, exports: 32 },
  { date: "25/03", imports: 110, exports: 28 },
  { date: "26/03", imports: 0, exports: 35 },
  { date: "27/03", imports: 0, exports: 42 },
  { date: "28/03", imports: 0, exports: 38 },
  { date: "29/03", imports: 0, exports: 35 },
  { date: "30/03", imports: 115, exports: 30 },
  { date: "31/03", imports: 0, exports: 25 },
];

// Category inventory data for pie chart
export const dashboardCategoryInventory = [
  { name: "Phân bón", value: 3500 },
  { name: "Thuốc trừ sâu", value: 2800 },
  { name: "Thuốc trừ cỏ", value: 1500 },
  { name: "Thuốc trừ bệnh", value: 2000 },
  { name: "Thuốc kích thích", value: 1200 },
  { name: "Phân hữu cơ", value: 2500 },
  { name: "Khác", value: 800 },
];

// Top customers with highest debt
export const dashboardTopCustomersDebt = [
  { name: "Công ty TNHH Nông sản Miền Tây", value: 18500000 },
  { name: "HTX Nông nghiệp Sông Hậu", value: 15200000 },
  { name: "Trang trại Phú Quý", value: 12800000 },
  { name: "Công ty CP Trung An", value: 9700000 },
  { name: "Nông trại Thịnh Phát", value: 8300000 },
];

// Top selling products
export const dashboardTopSellingProducts = [
  { name: "Phân NPK 20-20-15", value: 230 },
  { name: "Thuốc trừ sâu Virtako 40WG", value: 185 },
  { name: "Thuốc trừ cỏ Sofit 300EC", value: 160 },
  { name: "Phân bón lá Đầu Trâu MK", value: 145 },
  { name: "Thuốc kích thích sinh trưởng Atonik 1.8SL", value: 130 },
];

// Monthly comparison data for new chart
export const dashboardMonthlyComparison = [
  { month: "T1", imports: 850, exports: 780, profit: 42000000 },
  { month: "T2", imports: 940, exports: 850, profit: 48000000 },
  { month: "T3", imports: 1150, exports: 990, profit: 56000000 },
  { month: "T4", imports: 980, exports: 900, profit: 52000000 },
  { month: "T5", imports: 1250, exports: 1150, profit: 65000000 },
  { month: "T6", imports: 1100, exports: 950, profit: 58000000 },
];
