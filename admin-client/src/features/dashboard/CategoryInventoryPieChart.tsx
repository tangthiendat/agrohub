import { useQuery } from "@tanstack/react-query";
import { Chart, registerables } from "chart.js";
import React, { useEffect, useRef } from "react";
import { productStockService } from "../../services";

// Register Chart.js components
Chart.register(...registerables);

const CategoryInventoryPieChart: React.FC = () => {
  const chartRef = useRef<HTMLCanvasElement>(null);
  const chartInstance = useRef<Chart | null>(null);

  const { data: categoryInventoryStats } = useQuery({
    queryKey: ["product-stocks", "stats", "category-inventory"],
    queryFn: () => productStockService.getCategoryInventoryStats(),
    select: (data) => data.payload,
  });

  // Generate distinctive colors for pie chart segments
  const generateColors = (count: number) => {
    const baseColors = [
      "rgba(54, 162, 235, 0.8)",
      "rgba(255, 99, 132, 0.8)",
      "rgba(255, 206, 86, 0.8)",
      "rgba(75, 192, 192, 0.8)",
      "rgba(153, 102, 255, 0.8)",
      "rgba(255, 159, 64, 0.8)",
      "rgba(199, 199, 199, 0.8)",
    ];

    const colors = [];
    for (let i = 0; i < count; i++) {
      colors.push(baseColors[i % baseColors.length]);
    }

    return colors;
  };

  useEffect(() => {
    if (chartRef.current) {
      // Destroy previous chart instance if it exists
      if (chartInstance.current) {
        chartInstance.current.destroy();
      }

      const ctx = chartRef.current.getContext("2d");
      if (ctx) {
        chartInstance.current = new Chart(ctx, {
          type: "doughnut",
          data: {
            labels: categoryInventoryStats?.map((item) => item.label) ?? [],
            datasets: [
              {
                data: categoryInventoryStats?.map((item) => item.value) ?? [],
                backgroundColor: generateColors(
                  categoryInventoryStats?.length || 0,
                ),
                borderWidth: 1,
                borderColor: "#fff",
              },
            ],
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                position: "left",
              },
              tooltip: {
                callbacks: {
                  label: function (context) {
                    const label = context.label || "";
                    const value = context.raw;
                    const total = context.chart.data.datasets[0].data.reduce(
                      (acc: number, val) =>
                        acc + (typeof val === "number" ? val : 0),
                      0,
                    );
                    const percentage = Math.round(
                      ((value as number) / total) * 100,
                    );
                    return `${label}: ${value} (${percentage}%)`;
                  },
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
  }, [categoryInventoryStats]);

  return (
    <div style={{ height: "300px" }}>
      <canvas ref={chartRef}></canvas>
    </div>
  );
};

export default CategoryInventoryPieChart;
