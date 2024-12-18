package com.ynab.ui.addAccount

import java.math.BigDecimal

data class AddAccountState(
    val accountName:String="",
    val accountBalance: BigDecimal = BigDecimal("0"),
    val isAddInProgress: Boolean = false,
    val isAddSuccess: Boolean = false,
    val isAddError: Boolean = false,
    val errorMessage: String = ""
)
