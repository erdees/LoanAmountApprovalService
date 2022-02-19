package com.approvalservice.app.model.approval;

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
public class PendingLoan
{
    private String customerId;
    private long loanAmount;
    private List<String> approvers;
}
