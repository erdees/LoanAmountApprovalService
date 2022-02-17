package com.approvalservice.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Frequent API error responses
 */

@Getter
@AllArgsConstructor
public enum APIResponseCodes {

    WRONG_CUSTOMER_ID("Wrong customer id. Should be in 'XX-XXXX-XXX' pattern", false),
    EMPTY_CUSTOMER_ID("Customer ID cannot be empty", false),
    EMPTY_LOAN_AMOUNT("Empty loan amount field", false),
    WRONG_LOAN_APPROVERS("Loan approvers: should be no more than three persons", false),
    APPROVERS_NOT_FOUND("Approvers not found", false);

    private final String result;
    private final boolean positive;
}
