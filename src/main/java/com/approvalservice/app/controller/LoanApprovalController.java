package com.approvalservice.app.controller;

import com.approvalservice.app.enums.BusinessMessages;
import com.approvalservice.app.enums.ErrorMessages;
import com.approvalservice.app.model.reports.BasicContractsReport;
import com.approvalservice.app.model.reports.ContractsReport;
import com.approvalservice.app.model.approval.PendingLoan;
import com.approvalservice.app.model.response.BasicResponse;
import com.approvalservice.app.model.response.ErrorResponse;
import com.approvalservice.app.model.approval.Approval;
import com.approvalservice.app.service.LoanApprovalService;
import com.approvalservice.app.service.LoanStatisticsService;
import com.approvalservice.app.storage.LoanApproversStorage;
import com.approvalservice.app.util.CustomerIdChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Loan approval request rest controller
 */

@RestController
@RequestMapping("/api/loan")
public class LoanApprovalController
{
    private LoanApprovalService loanApprovalService;

    private LoanApproversStorage loanApproversStorage;

    private LoanStatisticsService loanStatisticsService;

    @Value("${service.loan.num-of-approvers}")
    private int numberOfApprovers;

    @Autowired
    private void setLoanApprovalService(LoanApprovalService loanApprovalService)
    {
        this.loanApprovalService = loanApprovalService;
    }

    @Autowired
    public void setLoanApproversStorage(LoanApproversStorage loanApproversStorage)
    {
        this.loanApproversStorage = loanApproversStorage;
    }

    @Autowired
    public void setLoanStatisticsService(LoanStatisticsService loanStatisticsService)
    {
        this.loanStatisticsService = loanStatisticsService;
    }

    /**
     * Loan approval request endpoint
     */
    @PostMapping("/request")
    public ResponseEntity<BasicResponse> createLoanRequest(@RequestBody PendingLoan request)
    {
        ResponseEntity<BasicResponse> responseEntity = checkPendingLoanBody(request);
        if (responseEntity != null)
        {
            return responseEntity;
        }

        BasicResponse result = loanApprovalService.createLoanApprovalRequest(request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Loan approval request endpoint
     */
    @PostMapping("/approval")
    public ResponseEntity<BasicResponse> approveLoanRequest(@RequestBody Approval request)
    {
        ResponseEntity<BasicResponse> responseEntity = checkApprovalRequest(request);
        if (responseEntity != null)
        {
            return responseEntity;
        }

        BasicResponse result = loanApprovalService.processPendingContract(request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Loan report endpoint
     */
    @GetMapping("/report")
    public ResponseEntity<BasicContractsReport> createRequest()
    {
        ContractsReport result = loanStatisticsService.getStatsOnLoans();

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BasicContractsReport(BusinessMessages.NO_CONTRACTS_FOR_STAT));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    private ResponseEntity<BasicResponse> checkPendingLoanBody(@RequestBody PendingLoan request)
    {
        ResponseEntity<BasicResponse> responseEntity = checkCustomerId(request.getCustomerId());
        if (responseEntity != null)
        {
            return responseEntity;
        }

        if (request.getApprovers().size() > numberOfApprovers)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(ErrorMessages.WRONG_LOAN_APPROVERS));
        }
        if (!loanApproversStorage.isApproversExist(request.getApprovers()))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(ErrorMessages.APPROVERS_NOT_FOUND));
        }

        return null;
    }

    private ResponseEntity<BasicResponse> checkApprovalRequest(@RequestBody Approval request)
    {
        ResponseEntity<BasicResponse> responseEntity = checkCustomerId(request.getCustomerId());
        if (responseEntity != null)
        {
            return responseEntity;
        }

        if (!loanApproversStorage.isApproverExist(request.getLoanApprover()))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(ErrorMessages.APPROVER_NOT_FOUND));
        }

        return null;
    }

    private ResponseEntity<BasicResponse> checkCustomerId(String customerId) {
        if (customerId == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(ErrorMessages.EMPTY_CUSTOMER_ID));
        }
        if (!CustomerIdChecker.isValid(customerId))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(ErrorMessages.WRONG_CUSTOMER_ID));
        }

        return null;
    }
}