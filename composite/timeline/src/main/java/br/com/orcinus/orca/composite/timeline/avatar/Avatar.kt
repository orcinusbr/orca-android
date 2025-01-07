/*
 * Copyright © 2023–2025 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.composite.timeline.avatar

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import br.com.orcinus.orca.autos.forms.Form
import br.com.orcinus.orca.autos.forms.Forms
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.composite.timeline.Units
import br.com.orcinus.orca.composite.timeline.avatar.interop.asViewOutlineProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.sample.image.AuthorImageSource
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.image.createSample
import br.com.orcinus.orca.platform.core.image.resourceID
import br.com.orcinus.orca.std.image.compose.ComposableImage
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import br.com.orcinus.orca.std.image.compose.SomeComposableImageLoader
import com.jeanbarrossilva.loadable.placeholder.Placeholder

/**
 * Tag that identifies an avatar composable for testing purposes.
 *
 * @see SmallAvatar
 * @see LargeAvatar
 */
internal const val AvatarTag = "avatar"

/**
 * Properties and behaviors of the rendering of the picture of a [Profile] based on its size:
 * - [SMALL], for contexts in which it is not one of the most important parts of the UI (e. g.,
 *   composition of comments or standalone posts); or
 * - [LARGE], targeting ones that have such [Profile] or its owner be highlighted in terms of
 *   prominence (e. g., their own page).
 */
@Immutable
private enum class Avatar {
  /**
   * Avatar size-based rendering characteristics for contexts in which it is not one of the most
   * prominent UI components.
   */
  SMALL {
    override val sizeThreshold = 42.dp
    override val form = Forms.default.medium as Form.PerCorner
  },

  /**
   * Avatar size-based rendering characteristics for contexts in which it is one of the most
   * prominent UI components.
   */
  LARGE {
    override val sizeThreshold = 128.dp
    override val form = Forms.default.large as Form.PerCorner
  };

  /**
   * [Form] that defines how the avatar is clipped.
   *
   * @see outline
   * @see shape
   */
  protected abstract val form: Form.PerCorner

  /**
   * Starting amount of density-dependent pixels that compose both the width and the height of the
   * avatar.
   */
  abstract val sizeThreshold: Dp

  /** [Shape] of an avatar of this size. */
  inline val shape: Shape
    get() = form.asShape

  /**
   * Defines the outline of the [view] and clips it to it.
   *
   * @param view [AvatarView] to be outlined.
   */
  fun outline(view: AvatarView) {
    view.outlineProvider = form.asViewOutlineProvider()
    view.clipToOutline = true
  }

  companion object {
    /**
     * Returns the avatar size class that is most suitable for the given [size].
     *
     * @param size Width and height of the avatar in absolute pixels.
     */
    @JvmStatic
    fun sized(size: Dp): Avatar {
      return entries.first { size.coerceAtLeast(it.sizeThreshold) >= it.sizeThreshold }
    }
  }
}

/**
 * [ImageView] for the picture of a [Profile].
 *
 * @param context [Context] in which it will be added.
 * @param attributeSet Attributes specified in XML.
 * @param defaultStyleAttribute Attribute of the style to be applied by default.
 */
class AvatarView
@JvmOverloads
constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  @AttrRes defaultStyleAttribute: Int = 0
) : AppCompatImageView(context, attributeSet, defaultStyleAttribute) {
  /** Size class of the avatar based on which it is laid out. */
  private var avatar = Avatar.SMALL
    set(avatar) {
      field = avatar
      field.outline(this)
    }

  init {
    if (isInEditMode) {
      setImageDrawable(ContextCompat.getDrawable(context, AuthorImageSource.Default.resourceID))
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val widthMode = MeasureSpec.getMode(widthMeasureSpec)
    val heightMode = MeasureSpec.getMode(heightMeasureSpec)
    val size =
      when {
        widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED ->
          maxOf(
            maxOf(suggestedMinimumWidth, suggestedMinimumHeight),
            Units.dp(context, avatar.sizeThreshold.value)
          )
        widthMode == MeasureSpec.AT_MOST ||
          widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.UNSPECIFIED ->
          maxOf(suggestedMinimumWidth, MeasureSpec.getSize(widthMeasureSpec))
        widthMode == MeasureSpec.UNSPECIFIED &&
          (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY) ->
          maxOf(suggestedMinimumHeight, MeasureSpec.getSize(heightMeasureSpec))
        else ->
          maxOf(
            maxOf(suggestedMinimumWidth, MeasureSpec.getSize(widthMeasureSpec)),
            maxOf(suggestedMinimumHeight, MeasureSpec.getSize(heightMeasureSpec))
          )
      }
    setMeasuredDimension(size, size)
    avatar = Avatar.sized(Units.px(context, size).dp)
  }

  companion object {
    /**
     * Creates a content description appropriate for an [AvatarView].
     *
     * @param context [Context] by which the [String] resource will be obtained.
     * @param name Name of the owner to which the avatar belongs.
     */
    @JvmStatic
    fun createContentDescription(context: Context, name: String) =
      context.getString(R.string.composite_timeline_avatar, name)
  }
}

/**
 * Loading [Profile] picture at a small size.
 *
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 */
@Composable
fun SmallAvatar(modifier: Modifier = Modifier) {
  Placeholder(
    modifier.requiredSize(Avatar.SMALL.sizeThreshold).testTag(AvatarTag),
    shape = Avatar.SMALL.shape
  )
}

/**
 * [Profile] picture at a small size.
 *
 * @param imageLoader [ComposableImageLoader] by which the avatar will be loaded.
 * @param name Name of the owner to which avatar belongs.
 * @param modifier [Modifier] to be applied to the underlying [ComposableImage].
 */
@Composable
fun SmallAvatar(
  imageLoader: SomeComposableImageLoader,
  name: String,
  modifier: Modifier = Modifier
) {
  imageLoader.load()(
    AvatarView.createContentDescription(LocalContext.current, name),
    Avatar.SMALL.shape,
    ComposableImageLoader.DefaultContentScale,
    modifier.requiredSize(Avatar.SMALL.sizeThreshold).testTag(AvatarTag)
  )
}

/**
 * Loading [Profile] picture at a large size.
 *
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 */
@Composable
fun LargeAvatar(modifier: Modifier = Modifier) {
  Placeholder(
    modifier.requiredSize(Avatar.LARGE.sizeThreshold).testTag(AvatarTag),
    shape = Avatar.LARGE.shape
  )
}

/**
 * [Profile] picture at a large size.
 *
 * @param imageLoader [ComposableImageLoader] by which the avatar will be loaded.
 * @param name Name of the owner to which avatar belongs.
 * @param modifier [Modifier] to be applied to the underlying [ComposableImage].
 */
@Composable
fun LargeAvatar(
  imageLoader: SomeComposableImageLoader,
  name: String,
  modifier: Modifier = Modifier
) {
  imageLoader.load()(
    AvatarView.createContentDescription(LocalContext.current, name),
    Avatar.LARGE.shape,
    ComposableImageLoader.DefaultContentScale,
    modifier.requiredSize(Avatar.LARGE.sizeThreshold).testTag(AvatarTag)
  )
}

/**
 * [Profile] picture of the default sample [Author] at a small size.
 *
 * This avatar is intended for previewing and testing only.
 *
 * @param modifier [Modifier] to be applied to the underlying [ComposableImage].
 * @see AuthorImageSource.Default
 */
@Composable
@VisibleForTesting
internal fun SampleSmallAvatar(modifier: Modifier = Modifier) {
  SmallAvatar(
    ComposableImageLoader.createSample(AuthorImageSource.Default),
    Author.sample.name,
    modifier
  )
}

/**
 * [Profile] picture of the default sample [Author] at a large size.
 *
 * This avatar is intended for previewing and testing only.
 *
 * @param modifier [Modifier] to be applied to the underlying [ComposableImage].
 * @see AuthorImageSource.Default
 */
@Composable
@VisibleForTesting
internal fun SampleLargeAvatar(modifier: Modifier = Modifier) {
  LargeAvatar(
    ComposableImageLoader.createSample(AuthorImageSource.Default),
    Author.sample.name,
    modifier
  )
}

@Composable
@MultiThemePreview
private fun LoadingLargeAvatarPreview() {
  AutosTheme { LargeAvatar() }
}

/** Preview of a loaded [LargeAvatar]. */
@Composable
@MultiThemePreview
private fun LoadedLargeAvatarPreview() {
  AutosTheme { SampleLargeAvatar() }
}

/** Preview of a loading [SmallAvatar]. */
@Composable
@MultiThemePreview
private fun LoadingSmallAvatarPreview() {
  AutosTheme { SmallAvatar() }
}

/** Preview of a loaded [SmallAvatar]. */
@Composable
@MultiThemePreview
private fun LoadedSmallAvatarPreview() {
  AutosTheme { SampleSmallAvatar() }
}
