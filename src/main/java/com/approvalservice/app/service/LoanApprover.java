package com.approvalservice.app.service;

import com.approvalservice.app.enums.APIBusinessMessages;
import com.approvalservice.app.model.request.LoanApprovalRequest;
import com.approvalservice.app.model.response.api.BasicResponse;
import com.approvalservice.app.model.response.approval.ApprovedLoan;
import com.approvalservice.app.storage.LoanApproversStorage;
import com.approvalservice.app.storage.LoanContractsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service which process approval request
 */

@Service
public class LoanApprover
{
    LoanApproversStorage loanApproversStorage;

    LoanContractsStorage loanContractsStorage;

    @Value("${service.loan.max-active-loans-per-contract}")
    private int activeContractLoans;

    @Autowired
    public void setLoanApproversStorage(LoanApproversStorage loanApproversStorage)
    {
        this.loanApproversStorage = loanApproversStorage;
    }

    @Autowired
    public void setLoanContractsStorage(LoanContractsStorage loanContractsStorage)
    {
        this.loanContractsStorage = loanContractsStorage;
    }

    public BasicResponse approveLoanRequest(LoanApprovalRequest request)
    {
        LocalDateTime now = LocalDateTime.now();

        List<String> approver = loanApproversStorage.getFirstApprover(request.getApprovers());
        String customerId = request.getCustomerId();

        calculateCreditScore();

        if (isNew(customerId))
        {
            ApprovedLoan response = prepareResponse(request, approver);

            response.setContractTime(now);

            loanContractsStorage.addNew(customerId, response);
            loanContractsStorage.setProcessing(customerId, false);

            return response;
        } else
            {
                if (loanContractsStorage.getCustomer(customerId).isProcessing())
                {
                    return new BasicResponse(APIBusinessMessages.CUSTOMER_CONTRACT_PENDING);
                } else
                    {
                        if (loanContractsStorage.getCustomer(customerId)
                                .getApprovedLoans().size() >= activeContractLoans)
                        {
                            return new BasicResponse(APIBusinessMessages.MAX_LOANS_PER_CUSTOMER);
                        }
                    }

                ApprovedLoan response = prepareResponse(request, approver);

                response.setContractTime(now);

                loanContractsStorage.updateExisting(customerId, response);
                loanContractsStorage.setProcessing(customerId, false);

                return response;
            }
    }

    private boolean isNew(String customerId)
    {
        return loanContractsStorage.isExists(customerId);
    }

    private ApprovedLoan prepareResponse(LoanApprovalRequest request, List<String> approver)
    {
        return new ApprovedLoan("Approved Loan", request.getCustomerId(), true,
                approver.get(0), request.getLoanAmount());
    }

    /**
     * Let's pretend we do some resource-intensive stuff like Camunda workflow or similar
     */
    private void calculateCreditScore()
    {
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
