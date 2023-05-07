package com.dox.tiptime

import org.junit.Assert.*


import org.junit.Test
import junit.framework.Assert.assertEquals

class TipCalculatorTests {
    @Test
    fun roundUpTip() {
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = "$2.00"
        val actualTip =calculateTip(amount = amount, tipPercent = tipPercent ,  false)
        assertEquals(expectedTip, actualTip)
    }
}