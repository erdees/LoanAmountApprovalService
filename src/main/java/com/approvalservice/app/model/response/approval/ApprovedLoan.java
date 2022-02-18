package com.approvalservice.app.model.response.approval;

import com.approvalservice.app.model.response.api.BasicResponse;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.time.LocalDateTime;

/**
 * Positive loan request response
 */

@Getter
@Setter
public class ApprovedLoan extends BasicResponse
{
    String userId;
    boolean approved;
    String approverName;
    long loanAmount;
    LocalDateTime contractTime;

    public ApprovedLoan(String message, String userId, boolean approved, String approverName, long loanAmount)
    {
        super(message);
        this.userId = userId;
        this.approved = approved;
        this.approverName = approverName;
        this.loanAmount = loanAmount;
    }

    @Transient
    public LocalDateTime getContractTime()
    {
        return contractTime;
    }
}
