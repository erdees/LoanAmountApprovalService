package com.approvalservice.app.model.response.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoanContractApprovalRequest
{
    private final String customerId;
    private final String loanApprover;
    private final boolean approved;
}
