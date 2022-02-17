package com.approvalservice.app.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerIdCheckerTest
{
    private static final String CASE1 = "XX-XXXX-XXX";
    private static final String CASE2 = "11-1111-111";
    private static final String CASE3 = "XX-XXXX-XXXX";
    private static final String CASE4 = "aaa";

    @Test
    void isValidPatternTest()
    {
        Assertions.assertTrue(CustomerIdChecker.isValid(CASE1));
        Assertions.assertTrue(CustomerIdChecker.isValid(CASE2));
        Assertions.assertFalse(CustomerIdChecker.isValid(CASE3));
        Assertions.assertFalse(CustomerIdChecker.isValid(CASE4));
    }
}
