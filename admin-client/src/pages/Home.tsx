import { useQuery } from "@tanstack/react-query";
import { Col, Row } from "antd";
import { useSearchParams } from "react-router";
import FilterTimeWithOutDate from "../common/components/FilterTimeWithOutDate";
import Loading from "../common/components/Loading";
import { useTitle } from "../common/hooks";
import CategoryInventoryPieChart from "../features/dashboard/CategoryInventoryPieChart";
import InventoryActivityChart from "../features/dashboard/InventoryActivityChart";
import StatsCard from "../features/dashboard/StatsCard";
import TopCustomersDebtChart from "../features/dashboard/TopCustomersDebtChart";
import {
  debtAccountService,
  exportInvoiceService,
  importInvoiceService,
  invoiceService,
} from "../services";

const Home: React.FC = () => {
  useTitle("Trang chủ | Dashboard");
  const [searchParams, setSearchParams] = useSearchParams();

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

  const handleDateChange = (
    startDate: string | null,
    endDate: string | null,
    type: string | null,
  ) => {
    if (startDate) {
      searchParams.set("startDate", startDate);
    } else {
      searchParams.delete("startDate");
    }

    if (endDate) {
      searchParams.set("endDate", endDate);
    } else {
      searchParams.delete("endDate");
    }

    if (type) {
      searchParams.set("type", type);
    } else {
      searchParams.delete("type");
    }

    setSearchParams(searchParams);
  };

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
          <InventoryActivityChart />
        </div>
      </div>

      {/* Secondary Charts - 2x2 Grid */}
      <Row gutter={[16, 16]}>
        <Col xs={24} md={12}>
          <div className="bg-white p-4 shadow">
            <h3 className="mb-4 text-lg font-semibold">
              Tỉ lệ tồn kho theo danh mục
            </h3>
            <CategoryInventoryPieChart />
          </div>
        </Col>
        <Col xs={24} md={12}>
          <div className="bg-white p-4 shadow">
            <h3 className="mb-4 text-lg font-semibold">
              Top 5 khách hàng có công nợ cao nhất
            </h3>
            <TopCustomersDebtChart />
          </div>
        </Col>
        {/* <Col xs={24} md={12}>
          <div className="bg-white p-4 shadow">
            <h3 className="mb-4 text-lg font-semibold">
              Top 5 sản phẩm bán chạy nhất
            </h3>
            <TopSellingProductsChart data={topSellingProducts} />
          </div>
        </Col> */}
      </Row>
    </div>
  );
};

export default Home;
