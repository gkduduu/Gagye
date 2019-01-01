package garye.utils.jhy.common;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class JApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "Maple.otf"))
                .addBold(Typekit.createFromAsset(this, "Maple_b.otf"));
    }

}
