package com.approvalservice.app.service;

import com.approvalservice.app.model.approval.Approval;
import com.approvalservice.app.model.approval.PendingLoan;
import com.approvalservice.app.model.response.BasicResponse;
import com.approvalservice.app.storage.LoanContractsStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanApprovalServiceTest
{
    @InjectMocks
    private LoanApprovalService loanApprovalService;

    @Mock
    private LoanContractsStorage loanContractsStorage;

    @Test
    void createLoanApprovalRequestTest()
    {
        ArrayList<String> approvers = new ArrayList<>(
                Arrays.asList("Mamertas Juronis", "Gaile Minderyte", "Nina Nugariene", "Dionizas Ulinskas"));
        PendingLoan loan = new PendingLoan("AA-1234-817", 5000, approvers);

        when(loanContractsStorage.isContractExists(anyString())).thenReturn(false);

        BasicResponse response = loanApprovalService.createLoanApprovalRequest(loan);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void processPendingContractTest()
    {
        String customerId = "21-Q2A1-XX9";
        String loanApprover = "Mamertas Juronis";
        boolean approved = true;

        Approval approval = new Approval(customerId, loanApprover, approved);

        when(loanContractsStorage.isContractExists(anyString())).thenReturn(true);
        when(loanContractsStorage.isPendingContracts(any(String.class))).thenReturn(true);
        when(loanContractsStorage.isApproveAllowed(any(String.class), any(String.class))).thenReturn(true);

        BasicResponse response = loanApprovalService.processPendingContract(approval);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
    }
}
