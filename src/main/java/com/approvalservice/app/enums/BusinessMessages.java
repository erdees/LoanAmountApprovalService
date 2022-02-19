package com.approvalservice.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Frequent API responses (reactions) on requests. Appears when the request was accepted.
 */

@Getter
@AllArgsConstructor
public enum BusinessMessages
{
    CUSTOMER_CONTRACT_PENDING("This contract currently in loan approval process. Please try again later."),
    CONTRACT_SENT_TO_APPROVAL("The contract has been sent to a loan approve manager."),
    MAX_LOANS_PER_CUSTOMER("This customer already have unsecured loans. Can't approve."),
    NO_CONTRACTS_FOR_STAT("No recently approved contracts found, try again later."),
    NO_PENDING_CONTRACTS("No pending contracts found for this customer."),
    CUSTOMER_NOT_FOUND("Customer with provided ID is not found."),
    NOT_APPROVED("Loan contract request was declined."),
    SUCCESSFULLY_SENT("Loan contract request was approved and successfully sent to the customer.");

    private final String result;
}
