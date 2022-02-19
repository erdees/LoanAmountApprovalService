package com.approvalservice.app.service;

import com.approvalservice.app.enums.APIBusinessMessages;
import com.approvalservice.app.model.request.LoanApprovalRequest;
import com.approvalservice.app.model.response.api.BasicResponse;
import com.approvalservice.app.model.response.approval.LoanContract;
import com.approvalservice.app.model.response.approval.LoanContractApprovalRequest;
import com.approvalservice.app.storage.LoanContractsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service which process approval request
 */

@Service
public class LoanApprovalService
{
    private LoanContractsStorage loanContractsStorage;

    @Autowired
    private void setLoanContractsStorage(LoanContractsStorage loanContractsStorage)
    {
        this.loanContractsStorage = loanContractsStorage;
    }

    /**
     * Here we prepare a loan contract for further approval
     */
    public BasicResponse createLoanApprovalRequest(LoanApprovalRequest request)
    {
        LocalDateTime createdDate = LocalDateTime.now();
        String customerId = request.getCustomerId();

        LoanContract preparedContract;

        if (isNew(customerId))
        {
            preparedContract = createPendingContract(request, createdDate);

            loanContractsStorage.createNewContract(customerId, preparedContract);
        } else
            {
                if (loanContractsStorage.getCustomer(customerId).isProcessing())
                {
                    return new BasicResponse(APIBusinessMessages.CUSTOMER_CONTRACT_PENDING);
                }

                preparedContract = createPendingContract(request, createdDate);

                loanContractsStorage.addLoanToContract(customerId, preparedContract);
            }

        loanContractsStorage.setProcessing(customerId, true);

        return new BasicResponse(APIBusinessMessages.CONTRACT_SENT_TO_APPROVAL);
    }

    private boolean isNew(String customerId)
    {
        return !loanContractsStorage.isContractExists(customerId);
    }

    private LoanContract createPendingContract(LoanApprovalRequest request, LocalDateTime createdDate)
    {
        LoanContract preparedContract = new LoanContract("Pending Loan", request.getCustomerId(),
                false, request.getApprovers(), request.getLoanAmount());
        preparedContract.setCreatedTime(createdDate);

        return preparedContract;
    }

    /**
     * Process incoming approval requests for loan managers. If request was approved, we assume that is was sent
     * to the customer, but this functionality is out of scope.
     */
    public BasicResponse processPendingContract(LoanContractApprovalRequest request)
    {
        LocalDateTime approvalTime = LocalDateTime.now();
        String customerId = request.getCustomerId();

        if (loanContractsStorage.isContractExists(customerId))
        {
            if (loanContractsStorage.isPendingContracts(customerId))
            {
                loanContractsStorage.setLoanApproval(customerId, request.isApproved(), approvalTime);
                loanContractsStorage.setProcessing(customerId, false);

                return new BasicResponse(String.valueOf(request.isApproved()));
            }
            else
            {
                return new BasicResponse("No pending contracts");
            }
        }
        else
        {
            return new BasicResponse("No customer");
        }
    }
}
