package ai.kwotly.companion.data.remote

/**
 * Carries a user-displayable message derived from a failed API call —
 * either the backend's `{ error }` envelope or a fallback for transport
 * failures. Repositories throw this so ViewModels can surface [message]
 * directly without inspecting HTTP internals.
 */
class KwotlyApiException(message: String) : Exception(message)
