package garye.utils.jhy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import garye.utils.jhy.common.JPreferenceManager;
import garye.utils.jhy.data.MainData;
import garye.utils.jhy.sheet.SheetUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class InputDialog extends Dialog {

    GoogleAccountCredential mCredential;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

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
        Calendar ca = Calendar.getInstance();
        DATE.setText(ca.get(Calendar.YEAR) + ". " + ca.get(Calendar.MONTH) + 1 + ". " + ca.get(Calendar.DATE));
        MONEY = findViewById(R.id.INPUT_MONEY);
        STORE = findViewById(R.id.INPUT_STORE);
        COMMENT = findViewById(R.id.INPUT_COMMENT);
        CARD = findViewById(R.id.INPUT_CARD);
        USER = findViewById(R.id.INPUT_USER);
        CATEGORY = findViewById(R.id.INPUT_CATE);
        SEND = findViewById(R.id.INPUT_SEND);

        SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("jhy","clickc!!!!!!!");
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

    private void connectSheet() {
        Log.i("jhy","connectSHeet!");
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            //디바이스 오프라인
        } else {
            new MakeRequestTask(mCredential,mMaindata).execute();
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
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                getContext(), android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = JPreferenceManager.getString(getContext(),PREF_ACCOUNT_NAME);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                connectSheet();
            } else {
                // Start a dialog from which the user can choose an account
                activity.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    activity,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private MainData mData;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential,MainData mainData) {
            Log.i("jhy","MakeRequestTask!");
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mData = mainData;
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.i("jhy","doInBackground!");
            try {
                return SheetUtils.putData(mService,mData);
            } catch (UserRecoverableAuthIOException e) {
                Log.i("jhy","doInBackground!"  + e.getMessage());
                mLastError = e;
                activity.startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
                e.getStackTrace();
                cancel(true);
                return null;
            } catch(Exception e) {
                mLastError = e;
                e.getStackTrace();
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            dismiss();
            super.onPostExecute(s);
        }
    }

}
