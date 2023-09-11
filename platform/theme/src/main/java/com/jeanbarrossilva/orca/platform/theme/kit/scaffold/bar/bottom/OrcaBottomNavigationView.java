package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.bottom;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jeanbarrossilva.orca.platform.theme.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/** Orca-specific {@link BottomNavigationView}. **/
public final class OrcaBottomNavigationView extends BottomNavigationView {
    /** {@link Paint} by which the divider will be painted. **/
    private final Paint dividerPaint = new Paint();

    /** Height of the divider in pixels. **/
    private final int dividerHeight;

    public OrcaBottomNavigationView(@NonNull Context context) {
        this(context, null);
    }

    public OrcaBottomNavigationView(
        @NonNull Context context,
        @Nullable AttributeSet attrs
    ) {
        this(context, attrs, 0);
    }

    public OrcaBottomNavigationView(
        @NonNull Context context,
        @Nullable AttributeSet attrs,
        int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        dividerPaint.setColor(context.getColor(R.color.placeholder));
        dividerHeight = Themes.isLight(context) ? Units.dp(context, 2) : 0;
        setBackgroundColor(getContext().getColor(R.color.surfaceContainer));
        stylizeItems();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
            MeasureSpec.getSize(heightMeasureSpec) + dividerHeight,
            MeasureSpec.getMode(heightMeasureSpec)
        );
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec) + dividerHeight
        );
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        drawDivider(canvas);
        super.onDraw(canvas);
    }

    /** Stylizes the items. **/
    private void stylizeItems() {
        setItemActiveIndicatorEnabled(false);
        setItemIconTintList(
            new ColorStateList(
                new int[][] {
                    new int[] { android.R.attr.state_selected },
                    new int[] { -android.R.attr.state_selected }
                },
                new int[] {
                    getContext().getColor(R.color.backgroundContent),
                    getContext().getColor(R.color.secondary)
                }
            )
        );
        setLabelVisibilityMode(LABEL_VISIBILITY_UNLABELED);
    }

    /** Draws the divider. **/
    private void drawDivider(@NonNull Canvas canvas) {
        canvas.drawRect(0f, 0f, getWidth(), dividerHeight, dividerPaint);
        canvas.translate(0f, dividerHeight);
    }
}
