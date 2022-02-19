package com.approvalservice.app.controller;

import com.approvalservice.app.enums.APIBusinessMessages;
import com.approvalservice.app.enums.APIResponseCodes;
import com.approvalservice.app.model.reports.BasicLoanContractsReport;
import com.approvalservice.app.model.reports.LoanContractsReport;
import com.approvalservice.app.model.request.LoanApprovalRequest;
import com.approvalservice.app.model.response.api.BasicResponse;
import com.approvalservice.app.model.response.api.ErrorResponse;
import com.approvalservice.app.model.response.approval.LoanContractApprovalRequest;
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
@RequestMapping("/api/loans")
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
    @PostMapping("/loan-approval-request")
    public ResponseEntity<BasicResponse> createLoanRequest(@RequestBody LoanApprovalRequest request)
    {
        ResponseEntity<BasicResponse> responseEntity = checkApprovalRequestBody(request);
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
    @PostMapping("/contract-approval")
    public ResponseEntity<BasicResponse> approveLoanRequest(@RequestBody LoanContractApprovalRequest request)
    {
        ResponseEntity<BasicResponse> responseEntity = checkContractApprovalRequest(request);
        if (responseEntity != null)
        {
            return responseEntity;
        }

        BasicResponse result = loanApprovalService.processPendingContract(request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Loan statistics endpoint
     */
    @GetMapping("/report")
    public ResponseEntity<BasicLoanContractsReport> createRequest()
    {
        LoanContractsReport result = loanStatisticsService.getStatsOnLoans();

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BasicLoanContractsReport(APIBusinessMessages.NO_CONTRACTS_FOR_STAT));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    private ResponseEntity<BasicResponse> checkApprovalRequestBody(@RequestBody LoanApprovalRequest request)
    {
        if (request.getCustomerId() == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(APIResponseCodes.EMPTY_CUSTOMER_ID));
        }
        if (!CustomerIdChecker.isValid(request.getCustomerId()))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(APIResponseCodes.WRONG_CUSTOMER_ID));
        }
        if (request.getApprovers().size() > numberOfApprovers)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(APIResponseCodes.WRONG_LOAN_APPROVERS));
        }

        if (!loanApproversStorage.isApproversExist(request.getApprovers()))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(APIResponseCodes.APPROVERS_NOT_FOUND));
        }

        return null;
    }

    private ResponseEntity<BasicResponse> checkContractApprovalRequest(@RequestBody LoanContractApprovalRequest request)
    {
        if (request.getCustomerId() == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(APIResponseCodes.EMPTY_CUSTOMER_ID));
        }
        if (!CustomerIdChecker.isValid(request.getCustomerId()))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(APIResponseCodes.WRONG_CUSTOMER_ID));
        }

        if (!loanApproversStorage.isApproverExist(request.getLoanApprover()))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(APIResponseCodes.APPROVER_NOT_FOUND));
        }

        return null;
    }
}