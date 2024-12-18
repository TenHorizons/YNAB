package com.ynab.ui.shared

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.displayTwoDecimal(): BigDecimal =
    this.setScale(2, RoundingMode.HALF_UP)
fun BigDecimal.isZero():Boolean =
    this.compareTo(BigDecimal.ZERO)==0
fun BigDecimal.isLessThanZero(): Boolean =
    this.compareTo(BigDecimal.ZERO)==-1