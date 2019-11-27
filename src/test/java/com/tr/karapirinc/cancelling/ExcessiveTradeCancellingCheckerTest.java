package com.tr.karapirinc.cancelling;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ExcessiveTradeCancellingChecker}
 */
public class ExcessiveTradeCancellingCheckerTest {

    @Test
    public void testCompaniesInvolvedInExcessiveCancellations() {
        assertEquals(ExcessiveTradeCancellingChecker.companiesInvolvedInExcessiveCancellations(), Arrays.asList("Ape accountants", "Cauldron cooking"));
    }

}
