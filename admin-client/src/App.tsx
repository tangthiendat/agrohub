import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { ConfigProvider } from "antd";
import viVN from "antd/locale/vi_VN";
import dayjs from "dayjs";
import "dayjs/locale/vi";
import isBetween from "dayjs/plugin/isBetween";
import timezone from "dayjs/plugin/timezone";
import utc from "dayjs/plugin/utc";
import { Toaster } from "react-hot-toast";
import {
  COLOR_PRIMARY,
  COLOR_SECONDARY,
  VIETNAM_TIMEZONE,
} from "./common/constants";
import AppRouter from "./router/AppRouter";

dayjs.locale("vi");
dayjs.extend(utc);
dayjs.extend(timezone);
dayjs.extend(isBetween);
dayjs.tz.setDefault(VIETNAM_TIMEZONE);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5,
    },
  },
});
function App() {
  return (
    <ConfigProvider
      locale={viVN}
      theme={{
        token: {
          colorPrimary: COLOR_PRIMARY,
          borderRadius: 0,
          colorLink: COLOR_PRIMARY,
          colorLinkHover: COLOR_SECONDARY,
        },
        components: {
          Table: {
            headerBg: COLOR_PRIMARY,
            headerColor: "#fff",
            headerSortActiveBg: COLOR_PRIMARY,
            headerSortHoverBg: COLOR_PRIMARY,
          },
        },
      }}
    >
      <QueryClientProvider client={queryClient}>
        <ReactQueryDevtools initialIsOpen={false} />
        <AppRouter />
      </QueryClientProvider>
      <Toaster
        position="top-center"
        containerStyle={{
          marginTop: "0.25rem",
        }}
        toastOptions={{
          success: {
            duration: 3000,
          },
          error: {
            duration: 3000,
          },
          style: {
            fontSize: "1rem",
            padding: "0.75rem 1rem",
          },
        }}
      />
    </ConfigProvider>
  );
}

export default App;
