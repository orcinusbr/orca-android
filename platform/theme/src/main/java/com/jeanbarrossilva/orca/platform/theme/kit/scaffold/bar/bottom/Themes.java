package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.bottom;

import android.content.Context;
import android.content.res.Configuration;

/** Utilities for performing theme-related operations. **/
class Themes {
    private Themes() {
    }

    /**
     * Checks whether the theme is light in the given {@link Context}.
     *
     * @param context {@link Context} whose theme's lightness will be checked.
     **/
    static boolean isLight(Context context) {
        int uiMode =
            context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return uiMode == Configuration.UI_MODE_NIGHT_NO;
    }
}
