import { useQuery } from "@tanstack/react-query";
import { Chart, registerables } from "chart.js";
import dayjs from "dayjs";
import React, { useEffect, useRef } from "react";
import { useSearchParams } from "react-router";
import { StatisticFilterCriteria } from "../../interfaces";
import { invoiceService } from "../../services";
import { formatTimeLabel } from "../../utils/datetime";

// Register Chart.js components
Chart.register(...registerables);

const InventoryActivityChart: React.FC = () => {
  const chartRef = useRef<HTMLCanvasElement>(null);
  const chartInstance = useRef<Chart | null>(null);
  const [searchParams] = useSearchParams();

  const filter: StatisticFilterCriteria = {
    startDate: searchParams.get("startDate") || dayjs().format("YYYY-MM"),
    endDate: searchParams.get("endDate") || undefined,
    type: searchParams.get("endDate")
      ? "date"
      : searchParams.get("type") || "month",
  };

  const { data: activityStats } = useQuery({
    queryKey: ["invoices", "stats", "activity", filter].filter((key) => {
      if (typeof key === "string") {
        return key !== "";
      } else if (key instanceof Object) {
        return Object.values(key).some(
          (value) => value !== undefined && value !== "",
        );
      }
    }),
    queryFn: () => invoiceService.getActivityStats(filter),
    select: (data) => data.payload,
  });

  useEffect(() => {
    if (chartRef.current) {
      // Destroy previous chart instance if it exists
      if (chartInstance.current) {
        chartInstance.current.destroy();
      }

      const ctx = chartRef.current.getContext("2d");
      if (ctx) {
        chartInstance.current = new Chart(ctx, {
          type: "line",
          data: {
            labels:
              activityStats?.map((item) =>
                formatTimeLabel(item.label, filter.type || "month"),
              ) ?? [],
            datasets: [
              {
                label: "Nhập kho",
                data: activityStats?.map((item) => item.imports) ?? [],
                borderColor: "#4169E1",
                backgroundColor: "rgba(65, 105, 225, 0.2)",
                borderWidth: 2,
                fill: true,
                tension: 0.4,
              },
              {
                label: "Xuất kho",
                data: activityStats?.map((item) => item.exports) ?? [],
                borderColor: "#FF8C00",
                backgroundColor: "rgba(255, 140, 0, 0.2)",
                borderWidth: 2,
                fill: true,
                tension: 0.4,
              },
            ],
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                position: "top",
              },
              tooltip: {
                mode: "index",
                intersect: false,
              },
            },
            scales: {
              y: {
                beginAtZero: true,
                grid: {
                  drawBorder: false,
                },
              },
              x: {
                grid: {
                  display: false,
                },
              },
            },
          },
        });
      }
    }

    // Clean up chart instance on component unmount
    return () => {
      if (chartInstance.current) {
        chartInstance.current.destroy();
      }
    };
  }, [activityStats]);

  return (
    <div style={{ height: "400px" }}>
      <canvas ref={chartRef}></canvas>
    </div>
  );
};

export default InventoryActivityChart;
