package com.ynab.data.repository

interface Repository {
    /**Save all data in repository on logout or onDestroy*/
    fun saveAllData()
}