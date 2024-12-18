package com.ynab.ui.addAccount

import java.math.BigDecimal

data class AddAccountState(
    val accountName:String="",
    val accountBalance: BigDecimal = BigDecimal("0"),
    val isError: Boolean = false,
    val isAdded: Boolean = false,
    val errorMessage: String = ""
)
