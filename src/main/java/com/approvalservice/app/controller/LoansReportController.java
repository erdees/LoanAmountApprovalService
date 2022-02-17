package com.approvalservice.app.controller;

import com.approvalservice.app.enums.APIBusinessMessages;
import com.approvalservice.app.model.LoanContractsReport;
import com.approvalservice.app.model.response.BasicResponse;
import com.approvalservice.app.service.LoanStatBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Loan statistics rest controller
 */

@RestController
public class LoansReportController
{
    LoanStatBuilder loanStatBuilder;

    @Autowired
    public void setLoanStatBuilder(LoanStatBuilder loanStatBuilder)
    {
        this.loanStatBuilder = loanStatBuilder;
    }

    /**
     * Loan statistics endpoint
     */
    @GetMapping("/api/loans/stat")
    public ResponseEntity<?> createRequest()
    {
        LoanContractsReport result = loanStatBuilder.getStatsOnLoans();

        if (result.isEmpty()) {
            return ResponseEntity.status(200).body(new BasicResponse(APIBusinessMessages.NO_CONTRACTS_FOR_STAT));
        }
        else
        {
            return ResponseEntity.status(200).body(result);
        }
    }
}