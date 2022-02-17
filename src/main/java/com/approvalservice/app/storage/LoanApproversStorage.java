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

    public List<String> getFirstApprover(List<String> approvers)
    {
        ArrayList<String> result = new ArrayList<>();

        for (String line : approvers)
        {
            if (loanApprovers.contains(line))
            {
                result.add(line);
                return result;
            }
        }

        return result;
    }
}
