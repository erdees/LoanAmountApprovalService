package com.approvalservice.app.storage;

import com.approvalservice.app.model.ClientAccount;
import com.approvalservice.app.model.response.LoanApproveResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Access to a contracts and stat
 */

@Component
public class LoanContractsStorage
{
    private static final ConcurrentHashMap<String, ClientAccount> customerContracts = new ConcurrentHashMap<>();

    public boolean isExists(String customerId)
    {
        return !customerContracts.containsKey(customerId);
    }

    public void addNew(String customerId, LoanApproveResponse response)
    {
        customerContracts.computeIfAbsent(customerId,
                k -> new ClientAccount(new ArrayList<>(Collections.singletonList(response)), true));
    }

    public void updateExisting(String customerId, LoanApproveResponse response)
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

    public List<LoanApproveResponse> getAllContracts()
    {
        List<LoanApproveResponse> allLoans = new ArrayList<>();

        for(Map.Entry<String, ClientAccount> entry : customerContracts.entrySet()) {
            ClientAccount customerProfile = entry.getValue();

            allLoans.addAll(customerProfile.getApprovedLoans());
        }

        return allLoans;
    }
}
