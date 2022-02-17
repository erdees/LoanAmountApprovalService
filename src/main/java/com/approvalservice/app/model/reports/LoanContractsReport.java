package com.approvalservice.app.model.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.beans.Transient;

/**
 * Loan report according to the task
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoanContractsReport extends BasicLoanContractsReport
{
    long contractsCount;
    long loanAmountsSum;
    long avgLoanAmount;
    long maxLoanAmount;
    long minLoanAmount;

    @Transient
    public boolean isEmpty()
    {
        return !(contractsCount != 0 && loanAmountsSum != 0
                && avgLoanAmount != 0 && maxLoanAmount != 0 && minLoanAmount != 0);
    }

    @Transient
    @Override
    public String getMessage()
    {
        return message;
    }
}
