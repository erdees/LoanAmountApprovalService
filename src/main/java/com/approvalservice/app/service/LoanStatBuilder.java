package com.approvalservice.app.service;

import com.approvalservice.app.model.LoanContractsReport;
import com.approvalservice.app.model.response.LoanApproveResponse;
import com.approvalservice.app.storage.LoanContractsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Service which provides statistics
 */

@Service
public class LoanStatBuilder
{
    LoanContractsStorage loanContractsStorage;

    @Autowired
    public void setLoanContractsStorage(LoanContractsStorage loanContractsStorage)
    {
        this.loanContractsStorage = loanContractsStorage;
    }

    /**
     * Form an object with filled loan statistics
     */
    public LoanContractsReport getStatsOnLoans()
    {
        List<LoanApproveResponse> allLoans = loanContractsStorage.getAllContracts();
        List<Long> loanAmounts = allContractsMoney(allLoans);

        if (!allLoans.isEmpty())
        {
            long contractsCount = allLoans.size();
            long loanAmountsSum = getLoanAmountsSum(allLoans);
            long avgLoanAmount = getAvgLoanAmount(loanAmounts);
            long maxLoanAmount = getMaxLoanAmount(loanAmounts);
            long minLoanAmount = getMinLoanAmount(loanAmounts);

            return new LoanContractsReport(contractsCount, loanAmountsSum, avgLoanAmount, maxLoanAmount, minLoanAmount);
        }
        else
        {
            return new LoanContractsReport();
        }
    }

    private long getLoanAmountsSum(List<LoanApproveResponse> allLoans)
    {
        long allLoansSum = 0;

        for (LoanApproveResponse contract : allLoans)
        {
            allLoansSum = allLoansSum + contract.getLoanAmount();
        }

        return allLoansSum;
    }

    private long getAvgLoanAmount(List<Long> loanAmounts)
    {
        OptionalDouble result =  loanAmounts.stream().mapToLong(l -> l).average();

        return (long) result.orElse(0);
    }

    private long getMaxLoanAmount(List<Long> loanAmounts)
    {
        return Collections.max(loanAmounts);
    }

    private long getMinLoanAmount(List<Long> loanAmounts)
    {
        return Collections.min(loanAmounts);
    }

    /**
     * Create a list with all amounts of each issued loan for further calculation
     */
    private List<Long> allContractsMoney(List<LoanApproveResponse> allLoans)
    {
        List<Long> loanAmounts = new ArrayList<>();

        for (LoanApproveResponse contract : allLoans)
        {
            loanAmounts.add(contract.getLoanAmount());
        }

        return loanAmounts;
    }
}
