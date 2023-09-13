package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.bottom;

import android.content.Context;
import android.util.TypedValue;
import androidx.annotation.NonNull;

/** Utilities for performing operations with units. **/
class Units {
    private Units() {
    }

    /**
     * Converts the DPs into pixels.
     *
     * @param context {@link Context} through which the conversion will take place.
     * @param dp Amount in DPs to be converted into pixels.
     */
    static int dp(@NonNull Context context, int dp) {
        return (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.getResources().getDisplayMetrics()
        );
    }
}
