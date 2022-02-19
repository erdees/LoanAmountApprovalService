package com.approvalservice.app.storage;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Access to a loan approvers
 */

@Component
public class LoanApproversStorage
{
    private static final Set<String> loanApprovers = new HashSet<>();

    static {
        // Generated from https://www.fantasynamegenerators.com/lithuanian-names.php
        loanApprovers.add("Mamertas Juronis");
        loanApprovers.add("Albertas Kanisauskas");
        loanApprovers.add("Juozapas Vaira");
        loanApprovers.add("Dionizas Ulinskas");
        loanApprovers.add("Giedra Pratusiene");
        loanApprovers.add("Vida Kvedaraite");
        loanApprovers.add("Salemonas Jotautas");
        loanApprovers.add("Sigute Kregzdyte");
        loanApprovers.add("Nina Nugariene");
        loanApprovers.add("Gaile Minderyte");
    }

    /**
     * Check if approvers exist for an initial loan approval request
     */
    public boolean isApproversExist(List<String> approvers)
    {
        ArrayList<String> requestApprovers = new ArrayList<>();

        for (String line : approvers)
        {
            if (loanApprovers.contains(line))
            {
                requestApprovers.add(line);
            }
            else
            {
                return false;
            }
        }

        return !requestApprovers.isEmpty();
    }

    /**
     * Checking if approver exist for pending loan contracts
     */
    public boolean isApproverExist(String approver)
    {
        return loanApprovers.contains(approver);
    }
}
