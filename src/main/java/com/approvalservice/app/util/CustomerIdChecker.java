package com.approvalservice.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Test customer ID String pattern
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerIdChecker
{
    private static final String ID_PATTERN = "[a-zA-Z0-9]{2}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{3}";

    public static boolean isValid(String input)
    {
        return input.matches(ID_PATTERN);
    }
}
