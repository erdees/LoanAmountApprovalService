package com.approvalservice.app.model.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Request we fill when send to approval
 */

@Getter
@Setter
@RequiredArgsConstructor
public class LoanApprovalRequest
{
    private String customerId;
    private long loanAmount;
    private List<String> approvers;
}
