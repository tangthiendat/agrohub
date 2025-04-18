import React, { useEffect, useRef } from "react";
import { Chart, registerables } from "chart.js";
import { formatCurrency } from "../../utils/number";

// Register Chart.js components
Chart.register(...registerables);

interface CustomerDebt {
  name: string;
  value: number;
}

interface TopCustomersDebtChartProps {
  data: CustomerDebt[];
}

const TopCustomersDebtChart: React.FC<TopCustomersDebtChartProps> = ({
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
          "#FF9900",
          "#6236FF",
          "#50CD89",
          "#009EF7",
          "#F1416C",
          "#3699FF",
          "#6DD400",
          "#FFA800",
          "#7239EA",
        ];

        chartInstance.current = new Chart(ctx, {
          type: "bar",
          data: {
            // Don't truncate labels - we'll handle wrapping in the options
            labels: sortedData.map((item) => item.name),
            datasets: [
              {
                label: "Công nợ",
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
                    return `Công nợ: ${formatCurrency(context.raw as number)}`;
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
                ticks: {
                  callback: function (value) {
                    const num = Number(value);
                    return num >= 1000000
                      ? (num / 1000000).toFixed(0) + "M"
                      : num >= 1000
                        ? (num / 1000).toFixed(0) + "K"
                        : num;
                  },
                },
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

export default TopCustomersDebtChart;
