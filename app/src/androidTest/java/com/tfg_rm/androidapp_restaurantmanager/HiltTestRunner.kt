package com.tfg_rm.androidapp_restaurantmanager

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Custom test runner that replaces the Application with [HiltTestApplication],
 * enabling Hilt dependency injection in instrumented tests annotated with
 * [@HiltAndroidTest][dagger.hilt.android.testing.HiltAndroidTest].
 *
 * Configured in app/build.gradle.kts as the testInstrumentationRunner.
 */
class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        name: String?,
        context: Context?
    ): Application = super.newApplication(cl, HiltTestApplication::class.java.name, context)
}
