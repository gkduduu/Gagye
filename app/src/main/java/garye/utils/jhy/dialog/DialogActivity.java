package garye.utils.jhy.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import garye.utils.jhy.BaseActivity;

/**
 * Created by hayoung on 03/01/2019.
 * gkduduu@naver.com
 */
public class DialogActivity extends BaseActivity {
    boolean firstFlag = true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firstFlag) {
            InputDialog dialog = new InputDialog(this);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    DialogActivity.this.finish();
                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    DialogActivity.this.finish();
                }
            });
            dialog.show();
            firstFlag = false;
        }
    }
}
