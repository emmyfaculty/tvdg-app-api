package com.tvdgapp.dtos.affiliate;

import lombok.Data;

@Data
public class AffiliateDashBoardDto {
    private int totalReferralNumber;
    private double totalSalesAmount;
    private double totalEarnings;
    private double totalWithdrawnBalance;
    private double totalRemainingBalance;

}

