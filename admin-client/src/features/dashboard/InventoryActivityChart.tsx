import React, { useEffect, useRef } from "react";
import { Chart, registerables } from "chart.js";

// Register Chart.js components
Chart.register(...registerables);

interface DataPoint {
  date: string;
  imports: number;
  exports: number;
}

interface InventoryActivityChartProps {
  data: DataPoint[];
}

const InventoryActivityChart: React.FC<InventoryActivityChartProps> = ({
  data,
}) => {
  const chartRef = useRef<HTMLCanvasElement>(null);
  const chartInstance = useRef<Chart | null>(null);

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
            labels: data.map((item) => item.date),
            datasets: [
              {
                label: "Nhập kho",
                data: data.map((item) => item.imports),
                borderColor: "#4169E1",
                backgroundColor: "rgba(65, 105, 225, 0.2)",
                borderWidth: 2,
                fill: true,
                tension: 0.4,
              },
              {
                label: "Xuất kho",
                data: data.map((item) => item.exports),
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
  }, [data]);

  return (
    <div style={{ height: "400px" }}>
      <canvas ref={chartRef}></canvas>
    </div>
  );
};

export default InventoryActivityChart;
