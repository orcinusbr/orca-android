package com.jeanbarrossilva.orca.core.feed.profile.toot.content.muting

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first

/**
 * Mutes and retrieves terms that have been muted.
 *
 * An instance of this class can be created via its factory method.
 */
abstract class TermMuter private constructor() {
  /**
   * Whether the [content] contains muted terms.
   *
   * @param content [Content] whose terms will be verified.
   */
  suspend fun isMuted(content: Content): Boolean {
    val terms = getTerms().first()
    return terms.any { it in content.text }
  }

  @TermMuterDsl
  class Builder internal constructor() {
    /**
     * Lambda to be invoked when the [Flow] to which the muted terms are emitted is requested to be
     * returned.
     */
    private var onGetTerms = { emptyFlow<List<String>>() }

    /** Lambda to be invoked when a term is requested to be muted. */
    private var onMute: suspend (term: String) -> Unit = {}

    /** Lambda to be invoked when a term is requested to be unmuted. */
    private var onUnmute: suspend (term: String) -> Unit = {}

    /**
     * Defines [getTerms] as the lambda to be invoked when the [Flow] with the terms that have been
     * muted is requested to be returned.
     *
     * @param getTerms Provides the [Flow] to which the muted terms are emitted.
     */
    fun getTerms(getTerms: () -> Flow<List<String>>): Builder {
      return apply { onGetTerms = getTerms }
    }

    /**
     * Defines [mute] as the lambda to be invoked when the a term is requested to be muted.
     *
     * @param mute Operation to be performed that mutes the given term.
     */
    fun mute(mute: suspend (term: String) -> Unit): Builder {
      return apply { onMute = mute }
    }

    /**
     * Defines [unmute] as the lambda to be invoked when the a term is requested to be unmuted.
     *
     * @param unmute Operation to be performed that unmutes the given term.
     */
    fun unmute(unmute: suspend (term: String) -> Unit): Builder {
      return apply { onUnmute = unmute }
    }

    /** Builds the [TermMuter] with the provided configuration. */
    internal fun build(): TermMuter {
      return object : TermMuter() {
        override fun getTerms(): Flow<List<String>> {
          return onGetTerms()
        }

        override suspend fun mute(term: String) {
          onMute(term)
        }

        override suspend fun unmute(term: String) {
          onUnmute(term)
        }
      }
    }
  }

  /** Gets the [Flow] to which the muted terms are emitted. */
  abstract fun getTerms(): Flow<List<String>>

  /**
   * Mutes the given [term].
   *
   * @param term Term to be muted.
   */
  abstract suspend fun mute(term: String)

  /**
   * Unmutes the given [term].
   *
   * @param term Term to be unmuted.
   */
  abstract suspend fun unmute(term: String)
}
