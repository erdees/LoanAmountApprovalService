package com.approvalservice.app.model;

import com.approvalservice.app.model.response.approval.LoanContract;
import lombok.*;

import java.util.List;

/**
 * Client account object. Will be stored for further statistics and analysis once a loan approved
 */

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientAccount
{
    private final List<LoanContract> loanContracts;
    private boolean processing;
}
