// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    //generated
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //----
    //hilt
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.kotlinAndroidKsp) apply false
    //----
    //Room
    alias(libs.plugins.room) apply false
}