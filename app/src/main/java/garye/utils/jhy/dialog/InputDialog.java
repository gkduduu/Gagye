package garye.utils.jhy.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import garye.utils.jhy.R;
import garye.utils.jhy.common.JConst;
import garye.utils.jhy.common.JPreferenceManager;
import garye.utils.jhy.data.MainData;
import garye.utils.jhy.sheet.SheetUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class InputDialog extends Dialog {

    GoogleAccountCredential mCredential;

    private static final String BUTTON_TEXT = "Call Google Sheets API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};

    TextView DATE;
    EditText MONEY;
    EditText STORE;
    EditText COMMENT;
    EditText CARD;
    EditText USER;
    EditText CATEGORY;
    Button SEND;

    MainData mMaindata;
    Activity activity;

    public InputDialog(@NonNull Activity context) {
        super(context);
        activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_input);

        mCredential = GoogleAccountCredential.usingOAuth2(
                getContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        DATE = findViewById(R.id.INPUT_DATE);
        final Calendar ca = Calendar.getInstance();
        int month = (ca.get(Calendar.MONTH) + 1);
        String strMonth = 10 > month ? "0" + month : month + "";
        DATE.setText(ca.get(Calendar.YEAR) + "-" + strMonth + "-" + ca.get(Calendar.DATE));
        DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(activity, listener, ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE));
                dialog.show();
            }
        });

        MONEY = findViewById(R.id.INPUT_MONEY);
        STORE = findViewById(R.id.INPUT_STORE);
        COMMENT = findViewById(R.id.INPUT_COMMENT);

        CARD = findViewById(R.id.INPUT_CARD);

        USER = findViewById(R.id.INPUT_USER);
        Log.i("jhy", Build.MODEL);
        if (Build.MODEL.contains("SM-N950N")) {
            USER.setText("하영");
        } else {
            USER.setText("소현");
        }
        CATEGORY = findViewById(R.id.INPUT_CATE);
        SEND = findViewById(R.id.INPUT_SEND);

        SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == MONEY.getText() || MONEY.getText().toString().equals("")) {
                    Toast.makeText(activity, "금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mMaindata = new MainData();
                mMaindata.setDate(DATE.getText().toString());
                mMaindata.setMoney(MONEY.getText().toString());
                mMaindata.setStore(STORE.getText().toString());
                mMaindata.setComment(COMMENT.getText().toString());
                mMaindata.setCard(CARD.getText().toString());
                mMaindata.setUser(USER.getText().toString());
                mMaindata.setCategory(CATEGORY.getText().toString());

                connectSheet();
            }
        });
    }

    //달력
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            String month = monthOfYear + "";
            if (monthOfYear < 10) {
                month = "0" + month;
            }
            DATE.setText(year + "-" + month + "-" + dayOfMonth);
        }
    };

    private void connectSheet() {
        Log.i("jhy", "connectSHeet!");
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            //디바이스 오프라인
        } else {
            new MakeRequestTask(mCredential, mMaindata).execute();
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(getContext());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(getContext());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                JConst.REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @AfterPermissionGranted(JConst.REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                getContext(), android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = JPreferenceManager.getString(getContext(), PREF_ACCOUNT_NAME);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                connectSheet();
            } else {
                // Start a dialog from which the user can choose an account
                activity.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        JConst.REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    activity,
                    "This app needs to access your Google account (via Contacts).",
                    JConst.REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private MainData mData;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential, MainData mainData) {
            Log.i("jhy", "MakeRequestTask!");
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mData = mainData;
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SEND.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.i("jhy", "doInBackground!");
            try {
                String a = SheetUtils.putData(mService, mData);
                ;
                return a;
//                return SheetUtils.getGagyeData(mService);
            } catch (UserRecoverableAuthIOException e) {
                Log.i("jhy", "doInBackground!" + e.getMessage());
                mLastError = e;
                activity.startActivityForResult(e.getIntent(), JConst.REQUEST_AUTHORIZATION);
                e.printStackTrace();
            } catch (Exception e) {
                mLastError = e;
                e.printStackTrace();
            } finally {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (null == s && null != mLastError) {
                Toast.makeText(activity, "실패! 문의바랍니다. <" + mLastError.getMessage() + ">", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        }
    }

}
