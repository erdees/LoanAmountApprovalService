package com.approvalservice.app.storage;

import com.approvalservice.app.model.ClientAccount;
import com.approvalservice.app.model.response.approval.LoanContract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Access to a contracts and statistics data.
 */

@Component
public class LoanContractsStorage
{
    @Value("${service.loan.sent-contracts-period}")
    private int sentContractsPeriod;

    private static final ConcurrentHashMap<String, ClientAccount> customerContracts = new ConcurrentHashMap<>();

    /**
     * Check if there is contracts for provided customer Id
     */
    public boolean isContractExists(String customerId)
    {
        return customerContracts.containsKey(customerId);
    }

    /**
     * Add new customer account with approved loan
     */
    public void createNewContract(String customerId, LoanContract response)
    {
        customerContracts.computeIfAbsent(customerId,
                k -> new ClientAccount(new ArrayList<>(Arrays.asList(response))));
    }

    /**
     * Update existing customer account with new loan contract
     */
    public void addLoanToContract(String customerId, LoanContract response)
    {
        if (customerContracts.containsKey(customerId)) {
            customerContracts.get(customerId).getLoanContracts().add(response);
        }
    }

    /**
     * Return customer account
     */
    public ClientAccount getCustomer(String customerId)
    {
        return customerContracts.get(customerId);
    }

    /**
     * Set processing flag which on customer's account. Will make further loan approval request not possible till
     * current unapproved loan will be processed by a loan manager.
     */
    public void setProcessing(String customerId, boolean processing)
    {
        if (customerContracts.containsKey(customerId))
        {
            customerContracts.get(customerId).setProcessing(processing);
        }
    }

    /**
     * Check if there is any pending unapproved loans
     */
    public boolean isPendingContracts(String customerId)
    {
        List<LoanContract> contracts = customerContracts.get(customerId).getLoanContracts();

        return contracts.stream().anyMatch(contract -> !contract.isApproved());
    }

    /**
     * Process loan prepared contract approval by a manager
     */
    public void setLoanApproval(String customerId, boolean approved, LocalDateTime approvalTime)
    {
        if (customerContracts.containsKey(customerId))
        {
            if (approved)
            {
                customerContracts.forEach((k, v) -> v.getLoanContracts().forEach(c -> {
                    c.setApprovedTime(approvalTime); c.setApproved(approved); }));
            }
            else
            {
                customerContracts.forEach((k,v) -> v.getLoanContracts().removeIf(c -> !c.isApproved()));
            }
        }
    }

    /**
     * Get list of approved loans (contracts) sent to the customer during amount of time configured in a config.
     */
    public List<LoanContract> getFilteredContracts(LocalDateTime time)
    {
        List<LoanContract> approvedContracts = new ArrayList<>();

        customerContracts.forEach((key, customerProfile) -> approvedContracts.addAll(customerProfile
                .getLoanContracts().stream()
                .filter(LoanContract::isApproved)
                .filter(loan -> loan.getApprovedTime().isAfter(time.minusSeconds(sentContractsPeriod)))
                .collect(Collectors.toList())));

        return approvedContracts;
    }
}
