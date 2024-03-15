/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.view

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.res.getStringOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.view.forEach
import com.google.android.material.navigation.NavigationBarView
import com.jeanbarrossilva.orca.platform.autos.R
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBarScope
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * Bar to which tabs for accessing different contexts within Orca can be added and/or a prominent
 * navigation action (such as going back to a previous context) may be performed.
 *
 * @param context [Context] in which this [NavigationBarView] is.
 * @param attributeSet [AttributeSet] from the XML tag by which this [NavigationBarView] is
 *   inflated.
 * @param defaultAttributeResource Attribute resource that supplies default values.
 * @throws IllegalArgumentException If an icon is specified but for the action but it hasn't been
 *   described.
 * @see NavigationBarScope.tab
 */
class NavigationBarView
@JvmOverloads
@Throws(IllegalArgumentException::class)
constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  @AttrRes defaultAttributeResource: Int = 0
) : AbstractComposeView(context, attributeSet, defaultAttributeResource) {
  /** [OnThemedCompositionListener] to be notified when the [Content] is composed and themed. */
  private var onThemedCompositionListener: OnThemedCompositionListener? = null

  /**
   * [NavigationBarScope] by which tabs are added.
   *
   * @see NavigationBarScope.tab
   */
  private val scope = NavigationBarScope()

  /** [MenuBuilder] to which the [MenuItem]s representing the tabs are added. */
  @Suppress("RestrictedApi") private val menuBuilder = MenuBuilder(context)

  /** ID resource of the currently selected tab. */
  @IdRes private var currentTabIDResource = 0

  /** [Drawable] of the action [ImageButton] icon. */
  private var actionIcon by mutableStateOf<Drawable?>(null)

  /** Description of what the action does. */
  private var actionContentDescription by mutableStateOf<String?>(null)

  /** [View.OnClickListener] to be notified of clicks on the action [ImageButton]. */
  private var onActionClickListener by mutableStateOf<OnClickListener?>(null)

  /** Describes the current context. */
  private var title by mutableStateOf("")
    @JvmName("_setTitle") set

  /**
   * ID of the action [ImageButton].
   *
   * @see ImageButton.getId
   */
  @VisibleForTesting internal val actionButtonID = View.generateViewId()

  /** ID of the title [TextView]. */
  @VisibleForTesting internal val titleViewID = View.generateViewId()

  /**
   * [Menu] to which the [MenuItem]s representing the tabs are added.
   *
   * @see NavigationBarScope.tab
   */
  val menu
    get() = menuBuilder as Menu

  /**
   * Listener that is notified when the [Content] is composed and themed.
   *
   * @see onThemedComposition
   */
  @VisibleForTesting
  internal fun interface OnThemedCompositionListener {
    /** Callback run when the [Content] is composed and themed. */
    @Composable @Suppress("ComposableNaming") fun onThemedComposition()
  }

  init {
    context.withStyledAttributes(
      attributeSet,
      R.styleable.NavigationBarView,
      defStyleAttr = defaultAttributeResource
    ) {
      setTitleFromAttribute()
      addTabsFromMenu()
      setActionFromAttributes()
    }
  }

  @Composable
  override fun Content() {
    AutosTheme {
      onThemedCompositionListener?.onThemedComposition()

      NavigationBar(
        scope,
        title = {
          LocalContentColor.current.toArgb().let { contentColorInArgb ->
            AndroidView({ TextView(it, null, 0, R.style.Theme_Orca_Typography_HeadlineLarge) }) {
              it.id = titleViewID
              it.text = title
              it.setTextColor(contentColorInArgb)
            }
          }
        },
        action = {
          LocalContentColor.current.toArgb().let { contentColorInArgb ->
            AndroidView(::ImageButton) { view ->
              view.id = actionButtonID
              view.background = null
              view.contentDescription = actionContentDescription
              view.imageTintList = ColorStateList.valueOf(contentColorInArgb)
              view.setOnClickListener(onActionClickListener)
              view.setImageDrawable(actionIcon)
            }
          }
        }
      )
    }
  }

  /**
   * Changes the title that describes the current context.
   *
   * @param title Title to be set.
   */
  fun setTitle(title: String) {
    this.title = title
  }

  /**
   * Adds a tab.
   *
   * @param idResource ID resource by which the tab will be identified.
   * @param iconResource Resource of the icon of the tab to be added.
   * @param contentDescriptionResource Resource of the description for the tab.
   * @param onClickListener [MenuItem.OnMenuItemClickListener] to be notified when the tab is
   *   clicked.
   * @see NavigationBarScope.tab
   */
  fun addTab(
    @IdRes idResource: Int,
    @DrawableRes iconResource: Int,
    @StringRes contentDescriptionResource: Int,
    onClickListener: MenuItem.OnMenuItemClickListener
  ) {
    @Suppress("RestrictedApi")
    menuBuilder
      .add(0, idResource, 0, null)
      ?.apply {
        contentDescription = context.getString(contentDescriptionResource)
        setOnMenuItemClickListener(onClickListener)
        setIcon(iconResource)
      }
      ?.run { scope.tab(this as MenuItemImpl) }
  }

  /**
   * Changes the tab that is currently selected.
   *
   * @param currentTabIDResource ID resource of the tab to be set as the current one.
   */
  fun setCurrentTab(@IdRes currentTabIDResource: Int) {
    this.currentTabIDResource = currentTabIDResource

    @Suppress("RestrictedApi")
    menu.findItem(currentTabIDResource).let { it as MenuItemImpl }.invoke()
  }

  /**
   * Updates the action [ImageButton].
   *
   * @param iconResource Resource of the [Drawable] of the icon to be set.
   * @param contentDescriptionResource Resource of the description to be set.
   * @param onClickListener [View.OnClickListener] to be notified when the [ImageButton] is clicked.
   */
  fun setAction(
    @DrawableRes iconResource: Int,
    @StringRes contentDescriptionResource: Int,
    onClickListener: OnClickListener
  ) {
    actionIcon = ContextCompat.getDrawable(context, iconResource)
    actionContentDescription = context.getString(contentDescriptionResource)
    onActionClickListener = onClickListener
  }

  /**
   * Changes the [OnThemedCompositionListener] to be notified when the [Content] is composed and
   * themed.
   *
   * @param onThemedCompositionListener [OnThemedCompositionListener] to be set.
   */
  @VisibleForTesting
  internal fun setOnCompositionListener(onThemedCompositionListener: OnThemedCompositionListener?) {
    this.onThemedCompositionListener = onThemedCompositionListener
  }

  /**
   * Changes the title to the one specified in the attribute.
   *
   * @see R.styleable.NavigationBarView_android_title
   */
  private fun TypedArray.setTitleFromAttribute() {
    getString(R.styleable.NavigationBarView_android_title)?.run(::setTitle)
  }

  /**
   * Adds tabs from the specified [Menu].
   *
   * @see R.styleable.NavigationBarView_menu
   * @see TypedArray.inflateMenu
   */
  private fun TypedArray.addTabsFromMenu() {
    @Suppress("PrivateResource")
    inflateMenu(context, menuBuilder, R.styleable.NavigationBarView_menu)

    menuBuilder.forEach { @Suppress("RestrictedApi") scope.tab(it as MenuItemImpl) }
  }

  /**
   * Changes the action with the specified attributes.
   *
   * @throws IllegalArgumentException If an icon is specified for the action but it hasn't been
   *   described.
   * @see R.styleable.NavigationBarView_actionIcon
   * @see R.styleable.NavigationBarView_actionContentDescription
   */
  private fun TypedArray.setActionFromAttributes() {
    getDrawable(R.styleable.NavigationBarView_actionIcon)?.let {
      actionIcon = it
      actionContentDescription =
        getStringOrThrow(R.styleable.NavigationBarView_actionContentDescription)
    }
  }
}
