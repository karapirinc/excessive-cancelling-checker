package com.tr.karapirinc.cancelling;

import com.tr.karapirinc.cancelling.model.TradeData;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class TradesDataCSVParserTest {

    @Test
    public void shouldParseTradesDataScv() {
        TradesDataCSVParser csvParser=new TradesDataCSVParser();
        Map<String, List<TradeData>> data = csvParser.parseTradesData();
        assertNotNull(data);
    }

}
