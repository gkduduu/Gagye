package garye.utils.jhy;


import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import garye.utils.jhy.common.AlldayNotification;

public class SettingFragment extends Fragment {

    CheckBox SET_NOTICHECK;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        SET_NOTICHECK = view.findViewById(R.id.SET_NOTICHECK);
        SET_NOTICHECK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    AlldayNotification.notify(getContext(), "asdf", 9);
                }else {
                    NotificationManager Nmang = (NotificationManager) getContext()
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    Nmang.cancelAll();
                }
            }
        });
        return view;
    }

}
