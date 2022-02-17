package com.approvalservice.app.model.response.approval;

import com.approvalservice.app.model.response.api.BasicResponse;
import lombok.Getter;

/**
 * Positive loan request response
 */

@Getter
public class ApprovedLoan extends BasicResponse
{
    String userId;
    boolean approved;
    String approverName;
    long loanAmount;

    public ApprovedLoan(String message, String userId, boolean approved, String approverName, long loanAmount)
    {
        super(message);
        this.userId = userId;
        this.approved = approved;
        this.approverName = approverName;
        this.loanAmount = loanAmount;
    }
}
