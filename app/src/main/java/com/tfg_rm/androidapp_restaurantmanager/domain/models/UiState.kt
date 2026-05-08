package com.tfg_rm.androidapp_restaurantmanager.domain.models

/**
 * A generic sealed class representing the various states of the User Interface.
 * * This class is used to wrap data and handle the UI lifecycle in a reactive way,
 * ensuring that the view can respond to different stages of data fetching
 * or background processing.
 * * @param T The type of data being held in the [Success] state.
 */
sealed class UiState<out T> {

    /**
     * Initial or resting state before any action has been triggered.
     */
    object Idle : UiState<Nothing>()

    /**
     * State indicating that an operation is currently in progress (e.g., fetching data).
     */
    object Loading : UiState<Nothing>()

    /**
     * State representing a successfully completed operation.
     * * @property data The resulting information retrieved or processed.
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * State representing a failure in the operation.
     * * @property message The string resource ID (R.string...) describing the error for the user.
     * @property throwable The optional exception or error details for debugging or logging.
     */
    data class Error(
        val message: Int,
        val throwable: Throwable? = null
    ) : UiState<Nothing>()
}