package com.approvalservice.app.storage;

import com.approvalservice.app.model.reports.ClientAccount;
import com.approvalservice.app.model.response.approval.ApprovedLoan;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Access to a contracts and statistics raw data. If the contract went to `customerContracts` Map, we assume that
 * approved contract was already sent to the customer.
 */

@Component
public class LoanContractsStorage
{
    private static final ConcurrentHashMap<String, ClientAccount> customerContracts = new ConcurrentHashMap<>();

    public boolean isExists(String customerId)
    {
        return !customerContracts.containsKey(customerId);
    }

    public void addNew(String customerId, ApprovedLoan response)
    {
        customerContracts.computeIfAbsent(customerId,
                k -> new ClientAccount(new ArrayList<>(Collections.singletonList(response)), true));
    }

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

    public List<ApprovedLoan> getAllContracts()
    {
        List<ApprovedLoan> allLoans = new ArrayList<>();

        customerContracts.forEach((key, customerProfile) -> allLoans.addAll(customerProfile.getApprovedLoans()));

        return allLoans;
    }
}
