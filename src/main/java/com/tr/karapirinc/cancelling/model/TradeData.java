package com.tr.karapirinc.cancelling.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TradeData {

    private String companyName;
    private LocalDateTime time;
    private OrderType orderType;
    private int quantity;

}
