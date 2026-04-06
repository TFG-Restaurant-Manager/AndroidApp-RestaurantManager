package com.tfg_rm.androidapp_restaurantmanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom [Application] class that serves as the base for the entire app.
 *
 * The [@HiltAndroidApp] annotation triggers Hilt's code generation, including
 * a base class for your application that serves as the application-level
 * dependency injection container. This is a mandatory requirement for
 * any project using Hilt for dependency management.
 *
 * This class is instantiated before any other process when the application
 * process is started.
 */
@HiltAndroidApp
class MyApp : Application()