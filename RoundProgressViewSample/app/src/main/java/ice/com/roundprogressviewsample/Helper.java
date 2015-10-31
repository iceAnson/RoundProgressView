package ice.com.roundprogressviewsample;

import android.content.Context;

/**
 * Created by lwh on 2015/10/15.
 */
public class Helper {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
