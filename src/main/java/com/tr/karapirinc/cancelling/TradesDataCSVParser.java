package com.tr.karapirinc.cancelling;

import com.tr.karapirinc.cancelling.model.OrderType;
import com.tr.karapirinc.cancelling.model.TradeData;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Parse Trades.data CSV and build TradeData object map
 */
//TODO Add a logger
class TradesDataCSVParser {

    private static final String FILE_NAME = "Trades.data";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, List<TradeData>> parseTradesData() {
        Map<String, List<TradeData>> data;

        try (Stream<String> stream = Files.lines(getFilePath())) {
            data = stream.map(l -> l.split(",")).map(mapToTradeData).filter(Objects::nonNull).collect(Collectors.groupingBy(TradeData::getCompanyName));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException("Corrupted File!");
        }

        return data;

    }

    private Path getFilePath() throws URISyntaxException {
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(FILE_NAME)).toURI());
    }

    private final Function<String[], TradeData> mapToTradeData = (line) -> {
        TradeData data;
        try {
            data = new TradeData();
            data.setTime(LocalDateTime.parse(line[0], TIME_FORMATTER));
            data.setCompanyName(line[1].trim());
            data.setOrderType(OrderType.valueOf(line[2].trim()));
            data.setQuantity(Integer.parseInt(line[3].trim()));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | DateTimeParseException e) {
            System.out.println("Corrupted data line! " + Arrays.toString(line)); //TODO Add a logger
            data = null;
        }
        return data;
    };
}
