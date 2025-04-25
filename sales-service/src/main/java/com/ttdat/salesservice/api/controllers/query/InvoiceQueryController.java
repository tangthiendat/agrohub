package com.ttdat.salesservice.api.controllers.query;

import com.ttdat.core.api.dto.response.*;
import com.ttdat.core.application.queries.exportinvoice.GetExportSummaryInRangeQuery;
import com.ttdat.core.application.queries.importinvoice.GetImportSummaryInRangeQuery;
import com.ttdat.core.application.queries.stats.GetExportInvoiceCountQuery;
import com.ttdat.core.application.queries.stats.GetImportInvoiceCountQuery;
import com.ttdat.core.infrastructure.utils.DateUtils;
import com.ttdat.core.infrastructure.utils.NumberUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/stats/card")
    public ApiResponse<StatsCardValue> getOrderStatsCard() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        GetExportInvoiceCountQuery getExportInvoiceCountQuery = GetExportInvoiceCountQuery.builder()
                .startDate(startOfMonth)
                .endDate(today)
                .build();
        Long currentExpInvoiceCount = queryGateway.query(getExportInvoiceCountQuery, ResponseTypes.instanceOf(Long.class)).join();
        GetImportInvoiceCountQuery getImportInvoiceCountQuery = GetImportInvoiceCountQuery.builder()
                .startDate(startOfMonth)
                .endDate(today)
                .build();
        Long currentImpInvoiceCount = queryGateway.query(getImportInvoiceCountQuery, ResponseTypes.instanceOf(Long.class)).join();
        BigDecimal currentTotalInvoiceCount = BigDecimal.valueOf(currentExpInvoiceCount + currentImpInvoiceCount);
        LocalDate startOfPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = startOfPreviousMonth.plusMonths(1).minusDays(1);
        GetExportInvoiceCountQuery getPreviousMonthExportInvoiceCountQuery = GetExportInvoiceCountQuery.builder()
                .startDate(startOfPreviousMonth)
                .endDate(endOfPreviousMonth)
                .build();
        Long previousMonthExportInvoiceCount = queryGateway.query(getPreviousMonthExportInvoiceCountQuery, ResponseTypes.instanceOf(Long.class)).join();
        GetImportInvoiceCountQuery getPreviousMonthImportInvoiceCountQuery = GetImportInvoiceCountQuery.builder()
                .startDate(startOfPreviousMonth)
                .endDate(endOfPreviousMonth)
                .build();
        Long previousMonthImportInvoiceCount = queryGateway.query(getPreviousMonthImportInvoiceCountQuery, ResponseTypes.instanceOf(Long.class)).join();
        BigDecimal previousTotalInvoiceCount = BigDecimal.valueOf(previousMonthExportInvoiceCount + previousMonthImportInvoiceCount);
        StatsCardValue statsCardValue = StatsCardValue.builder()
                .value(currentTotalInvoiceCount)
                .changePercentage(NumberUtils.getChangePercentage(previousTotalInvoiceCount, currentTotalInvoiceCount))
                .trend(NumberUtils.getTrendType(previousTotalInvoiceCount, currentTotalInvoiceCount))
                .build();
        return ApiResponse.<StatsCardValue>builder()
                .status(HttpStatus.OK.value())
                .message("Get invoice stats card successfully")
                .success(true)
                .payload(statsCardValue)
                .build();
    }

    @GetMapping("/stats/activity")
    public ApiResponse<List<ActivityChartData>> getActivityStats(@RequestParam Map<String, String> params) {
        String type = params.getOrDefault("type", "");
        String startDateStr = params.getOrDefault("startDate", null);
        String endDateStr = params.getOrDefault("endDate", null);

        Map<String, Activity> activityStats = new TreeMap<>();

      if (type.equals("year")) {
            int currentYear = LocalDate.now().getYear();
            int requestedYear = Integer.parseInt(startDateStr);
            if (currentYear >= requestedYear) {
                int currentMonth = LocalDate.now().getMonthValue();
                IntStream.rangeClosed(1, currentYear == requestedYear ? currentMonth : 12).forEach(month -> {
                    String key = YearMonth.of(currentYear, month).toString();
                    activityStats.put(key, Activity.builder()
                            .imports(BigDecimal.ZERO)
                            .exports(BigDecimal.ZERO)
                            .build());
                });
            }
        }

       if (type.equals("month")) {
            LocalDate now = LocalDate.now();
            int currentMonth = now.getMonthValue();
            int requestedMonth = Integer.parseInt(startDateStr.split("-")[1]);
            if (currentMonth >= requestedMonth) {
                YearMonth yearMonth = YearMonth.of(now.getYear(), requestedMonth);
                int currentDateOfMonth = (requestedMonth < currentMonth)
                        ? yearMonth.lengthOfMonth()
                        : now.getDayOfMonth();

                IntStream.rangeClosed(1, currentDateOfMonth).forEach(day -> {
                    String key = yearMonth.atDay(day).toString();
                    activityStats.put(key, Activity.builder()
                            .imports(BigDecimal.ZERO)
                            .exports(BigDecimal.ZERO)
                            .build());
                });
            }
        }

        if (type.equals("quarter")) {
            LocalDate startDate = DateUtils.parseDate(startDateStr, "quarter");
            LocalDate calculatedEndDate = startDate.plusMonths(3).withDayOfMonth(1).minusDays(1);
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.isBefore(calculatedEndDate) ? today : calculatedEndDate;

            while (!startDate.isAfter(endDate)) {
                String key = startDate.toString();
                activityStats.put(key, Activity.builder()
                        .imports(BigDecimal.ZERO)
                        .exports(BigDecimal.ZERO)
                        .build());
                startDate = startDate.plusDays(1);
            }
        }

        if (type.equals("date")) {
            LocalDate startDate = DateUtils.parseDate(startDateStr, "date");
            LocalDate endDate = DateUtils.parseDate(endDateStr, "date");
            LocalDate today = LocalDate.now();
            if (today.isBefore(endDate)) {
                endDate = today;
            }
            while (!startDate.isAfter(endDate)) {
                String key = startDate.toString();
                activityStats.put(key, Activity.builder()
                        .imports(BigDecimal.ZERO)
                        .exports(BigDecimal.ZERO)
                        .build());
                startDate = startDate.plusDays(1);
            }
        }

        GetImportSummaryInRangeQuery getImportSummaryInRangeQuery = GetImportSummaryInRangeQuery.builder()
                .startDateStr(startDateStr)
                .type(type)
                .build();
        if(endDateStr != null){
            getImportSummaryInRangeQuery.setEndDateStr(endDateStr);
        }

        List<ImportSummary> importSummaries = queryGateway.query(getImportSummaryInRangeQuery, ResponseTypes.multipleInstancesOf(ImportSummary.class)).join();

        importSummaries.forEach(importSummary -> {
            LocalDate createdDate = importSummary.getCreatedDate();
            String key = switch (type) {
                case "month", "quarter", "date" -> createdDate.toString();
                case "year" -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                    yield createdDate.format(formatter);
                }
                default -> throw new IllegalArgumentException("Invalid date type: " + type);
            };
            Activity activity = activityStats.get(key);
            if (activity != null) {
                activity.setImports(activity.getImports().add(importSummary.getTotalQuantity()));
            }
            activityStats.put(key, activity);
        });

        GetExportSummaryInRangeQuery getExportSummaryInRangeQuery = GetExportSummaryInRangeQuery.builder()
                .startDateStr(startDateStr)
                .type(type)
                .build();
        if(endDateStr != null){
            getExportSummaryInRangeQuery.setEndDateStr(endDateStr);
        }
        List<ExportSummary> exportSummaries = queryGateway.query(getExportSummaryInRangeQuery, ResponseTypes.multipleInstancesOf(ExportSummary.class)).join();

        exportSummaries.forEach(exportSummary -> {
            LocalDate createdDate = exportSummary.getCreatedDate();
            String key = switch (type) {
                case "month", "quarter", "date" -> createdDate.toString();
                case "year" -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                    yield createdDate.format(formatter);
                }
                default -> throw new IllegalArgumentException("Invalid date type: " + type);
            };
            Activity activity = activityStats.get(key);
            if (activity != null) {
                activity.setExports(activity.getExports().add(exportSummary.getTotalQuantity()));
            }
            activityStats.put(key, activity);
        });

       List<ActivityChartData> activityChartDataList = activityStats.keySet().stream()
                .map(key -> {
                    Activity activity = activityStats.get(key);
                    return ActivityChartData.builder()
                            .label(key)
                            .imports(activity != null ? activity.getImports() : BigDecimal.ZERO)
                            .exports(activity != null ? activity.getExports() : BigDecimal.ZERO)
                            .build();
                })
                .toList();

        return ApiResponse.<List<ActivityChartData>>builder()
                .status(HttpStatus.OK.value())
                .message("Get activity stats successfully")
                .success(true)
                .payload(activityChartDataList)
                .build();
    }
}
