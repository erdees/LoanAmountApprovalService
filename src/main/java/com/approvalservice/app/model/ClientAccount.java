package com.approvalservice.app.model;

import com.approvalservice.app.model.response.LoanApproveResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Client account object. Will be stored for further statistics and analysis once a loan approved
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientAccount
{
    private List<LoanApproveResponse> approvedLoans;
    private boolean processing;
}
