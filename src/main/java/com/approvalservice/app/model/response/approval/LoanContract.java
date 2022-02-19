package com.approvalservice.app.model.response.approval;

import com.approvalservice.app.model.response.api.BasicResponse;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Positive loan request response
 */

@Getter
@Setter
public class LoanContract extends BasicResponse
{
    private String userId;
    private boolean approved;
    private List<String> approverNames;
    private long loanAmount;
    private LocalDateTime createdTime;
    private LocalDateTime approvedTime;

    public LoanContract(String message, String userId, boolean approved, List<String> approverNames, long loanAmount)
    {
        super(message);
        this.userId = userId;
        this.approved = approved;
        this.approverNames = approverNames;
        this.loanAmount = loanAmount;
    }

    @Transient
    public LocalDateTime getCreatedTime()
    {
        return createdTime;
    }

    @Transient
    public LocalDateTime getApprovedTime()
    {
        return approvedTime;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanContract contract = (LoanContract) o;
        return approved == contract.approved &&
                loanAmount == contract.loanAmount &&
                userId.equals(contract.userId) &&
                approverNames.equals(contract.approverNames) &&
                Objects.equals(createdTime, contract.createdTime) &&
                Objects.equals(approvedTime, contract.approvedTime);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(userId, approved, approverNames, loanAmount, createdTime, approvedTime);
    }
}
