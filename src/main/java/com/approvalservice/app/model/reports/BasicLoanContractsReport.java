package com.approvalservice.app.model.reports;

import com.approvalservice.app.enums.APIBusinessMessages;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BasicLoanContractsReport
{
    private String message;

    public BasicLoanContractsReport(APIBusinessMessages message)
    {
        this.message = message.getResult();
    }
}
