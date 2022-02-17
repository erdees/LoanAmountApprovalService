package com.approvalservice.app.controller;

import com.approvalservice.app.enums.APIResponseCodes;
import com.approvalservice.app.model.request.LoanApprovalRequest;
import com.approvalservice.app.model.response.api.BasicResponse;
import com.approvalservice.app.model.response.api.ErrorResponse;
import com.approvalservice.app.service.LoanApprover;
import com.approvalservice.app.storage.LoanApproversStorage;
import com.approvalservice.app.util.CustomerIdChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Loan approval request rest controller
 */

@RestController
public class LoanApprovalRequestController
{
    LoanApprover loanApprovalService;

    LoanApproversStorage loanApproversStorage;

    @Value("${service.loan.num-of-approvers}")
    private int numberOfApprovers;

    @Autowired
    public void setLoanApprovalService(LoanApprover loanApprovalService)
    {
        this.loanApprovalService = loanApprovalService;
    }

    @Autowired
    public void setLoanApproversStorage(LoanApproversStorage loanApproversStorage)
    {
        this.loanApproversStorage = loanApproversStorage;
    }

    /**
     * Loan approval request endpoint
     */
    @PostMapping("/api/loans/get")
    public ResponseEntity<BasicResponse> createRequest(@RequestBody LoanApprovalRequest request)
    {
        ResponseEntity<BasicResponse> responseEntity = checkRequestBody(request);
        if (responseEntity != null)
        {
            return responseEntity;
        }

        BasicResponse result = loanApprovalService.approveLoanRequest(request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Basic request entry checks before send the request to approval
     */
    private ResponseEntity<BasicResponse> checkRequestBody(@RequestBody LoanApprovalRequest request)
    {
        if (request.getCustomerId() == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(APIResponseCodes.EMPTY_CUSTOMER_ID));
        }
        else
            {
                if (!CustomerIdChecker.isValid(request.getCustomerId()))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ErrorResponse(APIResponseCodes.WRONG_CUSTOMER_ID));
            }
        }
        if (request.getApprovers().size() > numberOfApprovers)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(APIResponseCodes.WRONG_LOAN_APPROVERS));
        }
        else
            {
                List<String> approver = loanApproversStorage.getFirstApprover(request.getApprovers());
                if (approver.isEmpty())
                {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ErrorResponse(APIResponseCodes.APPROVERS_NOT_FOUND));
                }
            }

        return null;
    }
}