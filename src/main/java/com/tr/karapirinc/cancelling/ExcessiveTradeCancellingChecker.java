package com.tr.karapirinc.cancelling;

import com.tr.karapirinc.cancelling.model.OrderType;
import com.tr.karapirinc.cancelling.model.TradeData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Checks which companies from the Trades.data are involved in excessive cancelling.
 */
final class ExcessiveTradeCancellingChecker {

    private final static float EXCESSIVE_CANCEL_RATIO = 1f / 3f;
    private final static int EXCESSIVE_CANCELLING_PERIOD = 60;//seconds


    private ExcessiveTradeCancellingChecker() {
    }

    /**
     * Returns the list of companies that are involved in excessive cancelling.
     */
    static List<String> companiesInvolvedInExcessiveCancellations() {

        List<String> result = new ArrayList<>();

        for (Map.Entry<String, List<TradeData>> companyDataMap : ExcessiveTradeCancellingChecker.getTradesData().entrySet()) {

            if (checkExcessiveCancellingInCompanyOrders(companyDataMap.getValue()))
                result.add(companyDataMap.getKey());
        }
        return result;
    }

    /**
     * Checks orders of a company for excessive cancelling
     * Groups orders by time
     *
     * @param companyTradesData Trades data of a company
     * @return boolean true if excessive cancelling found
     */
    private static boolean checkExcessiveCancellingInCompanyOrders(List<TradeData> companyTradesData) {
        Map<LocalDateTime, List<TradeData>> dataGroupedByTime = companyTradesData.stream().collect(Collectors.groupingBy(TradeData::getTime));

        return isThereExcessiveCancellingInData(dataGroupedByTime);
    }


    private static Map<String, List<TradeData>> getTradesData() {
        return new TradesDataCSVParser().parseTradesData();
    }

    /**
     * for every time line checks orders of next 60 seconds for excessive cancelling
     *
     * @param dataGroupedByTime Trade data grouped by time
     * @return boolean true if excessive cancelling found
     */
    private static boolean isThereExcessiveCancellingInData(Map<LocalDateTime, List<TradeData>> dataGroupedByTime) {

        for (LocalDateTime indexTime : dataGroupedByTime.keySet()) {
            if (checkExcessiveCancelInAPeriod(dataGroupedByTime, indexTime))
                return true;
        }

        return false;
    }

    /**
     * Checks excessive cancelling in a given period of data
     *
     * @param dataGroupedByTime Trade data grouped by time
     * @param indexTime Starting time of period
     * @return boolean true if excessive cancelling found
     */
    private static boolean checkExcessiveCancelInAPeriod(Map<LocalDateTime, List<TradeData>> dataGroupedByTime, LocalDateTime indexTime) {
        int cancelSum = 0;
        int newOrderSum = 0;
        final LocalDateTime periodEnd = indexTime.plusSeconds(EXCESSIVE_CANCELLING_PERIOD);

        for (LocalDateTime time : dataGroupedByTime.keySet()) {
            if (periodEnd.isAfter(time)) {
                for (TradeData data : dataGroupedByTime.get(time)) {
                    if (isItACancelOrder(data))
                        cancelSum += data.getQuantity();
                    else
                        newOrderSum += data.getQuantity();
                }
            } else {
                break;
            }
        }

        return checkOrderRatio(cancelSum, newOrderSum);
    }

    private static boolean checkOrderRatio(float cancelSum, float newOrderSum) {
        return (cancelSum / newOrderSum) > EXCESSIVE_CANCEL_RATIO;
    }

    private static boolean isItACancelOrder(TradeData data) {
        return OrderType.F.equals(data.getOrderType());
    }


    /**
     * Returns the total number of companies that are not involved in any excessive cancelling.
     */
    static int totalNumberOfWellBehavedCompanies() {
        //TODO Implement
        throw new UnsupportedOperationException("Not implemented, yet.");
    }

}
