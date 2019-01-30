package garye.utils.jhy.common;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class JConst {
    private static final JConst ourInstance = new JConst();

    public static final String SPREADSHEET_ID = "1uUayAOJlYP17jIxuZf3UsQA-sTLSfMcnYcur47ckNaE";

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    public static final int REALM_VERSION = 1;

    public GoogleAccountCredential getCredential() {
        return Credential;
    }

    public void setCredential(GoogleAccountCredential Credential) {
        this.Credential = Credential;
    }

    public GoogleAccountCredential Credential;

    public static JConst getInstance() {
        return ourInstance;
    }

    private JConst() {
    }
}
