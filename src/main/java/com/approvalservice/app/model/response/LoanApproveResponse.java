package com.approvalservice.app.model.response;

import lombok.Getter;

/**
 * Positive loan request response
 */

@Getter
public class LoanApproveResponse extends BasicResponse
{
    String userId;
    boolean approved;
    String approverName;
    long loanAmount;

    public LoanApproveResponse(String message, String userId, boolean approved, String approverName, long loanAmount)
    {
        super(message);
        this.userId = userId;
        this.approved = approved;
        this.approverName = approverName;
        this.loanAmount = loanAmount;
    }
}
