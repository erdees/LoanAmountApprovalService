package com.approvalservice.app.storage;

import com.approvalservice.app.model.reports.ClientAccount;
import com.approvalservice.app.model.response.approval.ApprovedLoan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Access to a contracts and statistics raw data. If the contract went to `customerContracts` Map, we assume that
 * approved contract was already sent to the customer.
 */

@Component
public class LoanContractsStorage
{
    @Value("${service.loan.sent-contracts-period}")
    private int sentContractsPeriod;

    private static final ConcurrentHashMap<String, ClientAccount> customerContracts = new ConcurrentHashMap<>();

    public boolean isExists(String customerId)
    {
        return !customerContracts.containsKey(customerId);
    }

    /**
     * Add new customer account with approved loan
     */
    public void addNew(String customerId, ApprovedLoan response)
    {
        customerContracts.computeIfAbsent(customerId,
                k -> new ClientAccount(new ArrayList<>(Collections.singletonList(response)), true));
    }

    /**
     * Update existing customer account with new loan contract
     */
    public void updateExisting(String customerId, ApprovedLoan response)
    {
        if (customerContracts.containsKey(customerId)) {
            customerContracts.get(customerId).getApprovedLoans().add(response);
            customerContracts.get(customerId).setProcessing(true);
        }
    }

    public ClientAccount getCustomer(String customerId)
    {
        return customerContracts.get(customerId);
    }

    public void setProcessing(String customerId, boolean processing)
    {
        if (customerContracts.containsKey(customerId))
        {
            customerContracts.get(customerId).setProcessing(processing);
        }
    }

    /**
     * Get list of approved loans (contracts) sent to the customer during amount of time configured in a config.
     */
    public List<ApprovedLoan> getFilteredContracts(LocalDateTime time)
    {
        List<ApprovedLoan> allLoans = getAllContracts();

        return filterLoansByTime(allLoans, time);
    }

    /**
     * Get list of all approved loans (contracts) sent to the customer
     */
    private List<ApprovedLoan> getAllContracts()
    {
        List<ApprovedLoan> allLoans = new ArrayList<>();

        customerContracts.forEach((key, customerProfile) -> allLoans.addAll(customerProfile.getApprovedLoans()));

        return allLoans;
    }

    /**
     * Filter list of approved loans by amount of time
     */
    private List<ApprovedLoan> filterLoansByTime(List<ApprovedLoan> contracts, LocalDateTime time)
    {
        List<ApprovedLoan> filteredContracts = new ArrayList<>();

        for (ApprovedLoan contract : contracts)
        {
            if (time.minusSeconds(sentContractsPeriod).isBefore(contract.getContractTime()))
            {
                filteredContracts.add(contract);
            }
        }

        return filteredContracts;
    }
}
