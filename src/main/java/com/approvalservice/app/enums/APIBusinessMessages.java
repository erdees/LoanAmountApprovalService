package com.approvalservice.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Frequent API error responses
 */

@Getter
@AllArgsConstructor
public enum APIBusinessMessages
{
    CUSTOMER_CONTRACT_PENDING("This contract currently in loan approval process. Please try again later."),
    CONTRACT_SENT_TO_APPROVAL("The contract has been sent to a loan approve manager."),
    MAX_LOANS_PER_CUSTOMER("This customer already have unsecured loans. Can't approve."),
    NO_CONTRACTS_FOR_STAT("No contracts found, try again later.");

    private final String result;
}
