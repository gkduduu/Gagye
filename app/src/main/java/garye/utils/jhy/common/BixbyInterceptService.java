package garye.utils.jhy.common;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * Created by hayoung on 21/02/2019.
 * gkduduu@naver.com
 */
public class BixbyInterceptService extends AccessibilityService {

    private static final int KEYCODE_BIXBY = 1082;

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KEYCODE_BIXBY &&
                event.getAction() == KeyEvent.ACTION_DOWN) {

            // Do your thing here; startActivity(), Toast, Notification, etc.
            Toast.makeText(this, "Bixby button pressed", Toast.LENGTH_SHORT).show();

            // Return true to stop the event from propagating further.
            return true;
        }

        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() {}
}
