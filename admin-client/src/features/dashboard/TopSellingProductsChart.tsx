import React, { useEffect, useRef } from "react";
import { Chart, registerables } from "chart.js";

// Register Chart.js components
Chart.register(...registerables);

interface ProductData {
  name: string;
  value: number;
}

interface TopSellingProductsChartProps {
  data: ProductData[];
}

const TopSellingProductsChart: React.FC<TopSellingProductsChartProps> = ({
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
        const sortedData = [...data].sort((a, b) => b.value - a.value);

        // Generate colors for bars
        const colors = [
          "#3699FF",
          "#6DD400",
          "#F1416C",
          "#FFA800",
          "#7239EA",
          "#FF9900",
          "#6236FF",
          "#50CD89",
          "#009EF7",
        ];

        chartInstance.current = new Chart(ctx, {
          type: "bar",
          data: {
            // Don't truncate labels - we'll handle wrapping in the options
            labels: sortedData.map((item) => item.name),
            datasets: [
              {
                label: "Số lượng bán",
                data: sortedData.map((item) => item.value),
                backgroundColor: colors.slice(0, sortedData.length),
                borderWidth: 0,
                borderRadius: 4,
                maxBarThickness: 30,
              },
            ],
          },
          options: {
            indexAxis: "y",
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                display: false,
              },
              tooltip: {
                callbacks: {
                  label: function (context) {
                    return `Số lượng: ${context.raw}`;
                  },
                },
              },
            },
            scales: {
              y: {
                grid: {
                  display: false,
                },
                // Configure the y-axis to display full text
                ticks: {
                  // Increase left margin to make room for long texts
                  padding: 5,
                  // Use automatic font size
                  autoSkip: false,
                  // Create line breaks for long text
                  callback: function (value) {
                    const label = this.getLabelForValue(value as number);
                    // Split label if longer than 15 chars
                    if (typeof label === "string" && label.length > 20) {
                      // Return multi-line label
                      const words = label.split(" ");
                      const lines = [];
                      let currentLine = words[0];

                      for (let i = 1; i < words.length; i++) {
                        if (currentLine.length + words[i].length < 20) {
                          currentLine += " " + words[i];
                        } else {
                          lines.push(currentLine);
                          currentLine = words[i];
                        }
                      }

                      lines.push(currentLine);
                      return lines;
                    }
                    return label;
                  },
                },
              },
              x: {
                grid: {
                  drawBorder: false,
                },
                beginAtZero: true,
              },
            },
            // Add more height for multiline labels
            layout: {
              padding: {
                left: 20,
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
    <div style={{ height: "320px" }}>
      <canvas ref={chartRef}></canvas>
    </div>
  );
};

export default TopSellingProductsChart;
