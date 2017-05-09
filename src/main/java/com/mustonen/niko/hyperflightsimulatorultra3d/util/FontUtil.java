package com.mustonen.niko.hyperflightsimulatorultra3d.util;

import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;

/**
 * Class for generating scifi font and loafing screen.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class FontUtil {

    /**
     * Generates sci fi font.
     *
     * @param context    Activity context.
     * @param text       Text.
     * @param align      Alignment.
     * @param startColor Starting color.
     * @param endColor   Ending color.
     * @return
     */
    public static final TextView generateSciFiText(
            Context context, String text, int align, String startColor, String endColor) {

        TextView textView = new TextView(context);
        textView.getPaint().setColor(Color.WHITE);
        textView.getPaint().setShadowLayer(20, -10, 10, Color.WHITE);
        textView.setText(text);
        textView.setPadding(150, 100, 150, 0);
        if (align != -50) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            textView.setLayoutParams(params);
            textView.setTextAlignment(align);
        }

        Shader textShader = new LinearGradient(0, 0, 0, 150,
                new int[]{Color.parseColor(startColor), Color.parseColor(endColor)},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

        textView.setTextSize(40);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/thunderstrike.ttf");
        textView.setTypeface(face);
        return textView;
    }

    /**
     * Generates loading screen.
     *
     * @param c Activity context.
     * @return Linear layout with loading text.
     */
    public static LinearLayout getLoadingScreen(Context c) {
        LinearLayout linearLayout = new LinearLayout(c);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        String startColor = "#ed21dc";
        String endColor = "#25aff9";

        TextView loading = FontUtil.generateSciFiText(c, " LOADING... ", -50, startColor, endColor);
        loading.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setBackgroundColor(Color.BLACK);
        linearLayout.addView(loading);
        return linearLayout;
    }

    /**
     * Returns screen to indicate that game saving is in progress.
     *
     * @param c Activity context.
     * @return LinearLayout that stores "svaing..." text.
     */
    public static LinearLayout getSavingScreen(Context c) {
        LinearLayout linearLayout = new LinearLayout(c);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        String startColor = "#ed21dc";
        String endColor = "#25aff9";

        TextView loading = FontUtil.generateSciFiText(c, " saving highscore to cloud... ",
                -50, startColor, endColor);
        loading.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setBackgroundColor(Color.BLACK);
        linearLayout.addView(loading);
        return linearLayout;
    }
}
