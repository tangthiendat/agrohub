import { Row, Col } from "antd";
import { useEffect, useState } from "react";
import { useTitle } from "../common/hooks";
import FilterTimeWithOutDate from "../common/components/FilterTimeWithOutDate";
import {
  dashboardCategoryInventory,
  dashboardCustomerDebtStats,
  dashboardExportStats,
  dashboardImportStats,
  dashboardInventoryActivity,
  dashboardOrderStats,
  dashboardTopCustomersDebt,
  dashboardTopSellingProducts,
} from "../features/dashboard/mock/dashboardData";
import StatsCard from "../features/dashboard/StatsCard";
import InventoryActivityChart from "../features/dashboard/InventoryActivityChart";
import CategoryInventoryPieChart from "../features/dashboard/CategoryInventoryPieChart";
import TopCustomersDebtChart from "../features/dashboard/TopCustomersDebtChart";
import TopSellingProductsChart from "../features/dashboard/TopSellingProductsChart";
import { useQuery } from "@tanstack/react-query";
import {
  debtAccountService,
  exportInvoiceService,
  importInvoiceService,
  invoiceService,
} from "../services";
import Loading from "../common/components/Loading";

const Home: React.FC = () => {
  useTitle("Trang chủ | Dashboard");

  const [dateFilter, setDateFilter] = useState({
    startDate: null,
    endDate: null,
    type: "month",
  });

  const { data: orderStats, isLoading: isOrderCountLoading } = useQuery({
    queryFn: () => invoiceService.getOrderStatsCard(),
    queryKey: ["invoices", "stats", "card"],
    select: (data) => data.payload,
  });

  const { data: customerDebtStats, isLoading: isCustomerDebtLoading } =
    useQuery({
      queryFn: () => debtAccountService.getCustomerDebtStatsCard(),
      queryKey: ["debt-accounts", "stats", "card"],
      select: (data) => data.payload,
    });

  const { data: importStats, isLoading: isImportLoading } = useQuery({
    queryFn: () => importInvoiceService.getImportStatsCard(),
    queryKey: ["import-invoices", "stats", "card"],
    select: (data) => data.payload,
  });

  const { data: exportStats, isLoading: isExportLoading } = useQuery({
    queryFn: () => exportInvoiceService.getExportStatsCard(),
    queryKey: ["export-invoices", "stats", "card"],
    select: (data) => data.payload,
  });

  const [inventoryActivity, setInventoryActivity] = useState(
    dashboardInventoryActivity,
  );
  const [categoryInventory, setCategoryInventory] = useState(
    dashboardCategoryInventory,
  );
  const [topCustomersDebt, setTopCustomersDebt] = useState(
    dashboardTopCustomersDebt,
  );
  const [topSellingProducts, setTopSellingProducts] = useState(
    dashboardTopSellingProducts,
  );

  // Handle date filter changes
  const handleDateChange = (
    startDate: string | null,
    endDate: string | null,
    type: string | null,
  ) => {
    setDateFilter({
      startDate,
      endDate,
      type: type || "range",
    });

    // In a real application, we would fetch new data based on the date filter
    console.log("Date filter changed:", startDate, endDate, type);
  };

  // // In a real application, we would fetch data when component mounts or filter changes
  // useEffect(() => {
  //   // Fetch data based on dateFilter
  // }, [dateFilter]);
  if (
    isOrderCountLoading ||
    isCustomerDebtLoading ||
    isImportLoading ||
    isExportLoading
  ) {
    return <Loading />;
  }

  return (
    <div className="dashboard-container p-6">
      {/* Stats Cards */}
      <Row gutter={[16, 16]} className="mb-6">
        {orderStats && (
          <Col xs={24} sm={12} md={6}>
            <StatsCard
              title="Tổng số đơn hàng"
              value={orderStats.value}
              changePercentage={orderStats.changePercentage}
              trend={orderStats.trend}
              icon="order"
            />
          </Col>
        )}
        {customerDebtStats && (
          <Col xs={24} sm={12} md={6}>
            <StatsCard
              title="Tổng công nợ khách hàng"
              value={customerDebtStats.value}
              isCurrency={true}
              changePercentage={customerDebtStats.changePercentage}
              trend={customerDebtStats.trend}
              icon="money"
            />
          </Col>
        )}
        {importStats && (
          <Col xs={24} sm={12} md={6}>
            <StatsCard
              title="Tổng nhập kho"
              value={importStats.value}
              changePercentage={importStats.changePercentage}
              trend={importStats.trend}
              icon="import"
            />
          </Col>
        )}
        <Col xs={24} sm={12} md={6}>
          {exportStats && (
            <StatsCard
              title="Tổng xuất kho"
              value={exportStats.value}
              changePercentage={exportStats.changePercentage}
              trend={exportStats.trend}
              icon="export"
            />
          )}
        </Col>
      </Row>

      {/* Main Chart */}
      <div className="mb-6">
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-xl font-bold">Biểu đồ hoạt động kho</h2>
          <FilterTimeWithOutDate onDateChange={handleDateChange} />
        </div>
        <div className="bg-white p-4 shadow">
          <InventoryActivityChart data={inventoryActivity} />
        </div>
      </div>

      {/* Secondary Charts - 2x2 Grid */}
      <Row gutter={[16, 16]}>
        <Col xs={24} md={12}>
          <div className="bg-white p-4 shadow">
            <h3 className="mb-4 text-lg font-semibold">
              Tỉ lệ tồn kho theo danh mục
            </h3>
            <CategoryInventoryPieChart data={categoryInventory} />
          </div>
        </Col>
        <Col xs={24} md={12}>
          <div className="bg-white p-4 shadow">
            <h3 className="mb-4 text-lg font-semibold">
              Top 5 khách hàng có công nợ cao nhất
            </h3>
            <TopCustomersDebtChart data={topCustomersDebt} />
          </div>
        </Col>
        <Col xs={24} md={12}>
          <div className="bg-white p-4 shadow">
            <h3 className="mb-4 text-lg font-semibold">
              Top 5 sản phẩm bán chạy nhất
            </h3>
            <TopSellingProductsChart data={topSellingProducts} />
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default Home;
