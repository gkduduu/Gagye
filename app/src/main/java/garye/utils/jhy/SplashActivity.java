package garye.utils.jhy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//https://www.google.co.kr/search?q=loading+gif&tbm=isch&tbs=rimg:CaIs9PeDXqbJIjhkPoWNMGNEVP1y07YtNLxsL7EPWPdIV3L8ypMSGOKvUcUAACDBcD1Fhex2na5j89hmek4gA-VaRSoSCWQ-hY0wY0RUEUcE5TaIypXZKhIJ_1XLTti00vGwRh9U_13E7YafoqEgkvsQ9Y90hXchHWkgCYuCGw4CoSCfzKkxIY4q9REXvzUdCggTw5KhIJxQAAIMFwPUURG0rNguZl4P8qEgmF7HadrmPz2BFxATTxNmp87ioSCWZ6TiAD5VpFEXd6ePIuFxXQ&tbo=u&sa=X&ved=2ahUKEwip8J6cl8zfAhUKwLwKHZZwDk4Q9C96BAgBEBs&biw=1920&bih=889&dpr=1
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Glide.with(this).asGif().load(R.drawable.loading).into((ImageView)findViewById(R.id.SPL_IMAGE));

//        Map<String, Object> user = new HashMap<>();
////        user.put("seq", "Ada");
//        user.put("date", Calendar.getInstance().getTime());
//        user.put("shop", "로또1등 당첨~~");
//        user.put("state", "수입!!");
//        user.put("money", "2,200,000,000원");
//        user.put("card", "하나통장");
//        user.put("comment", "댑악!!!!");
//        user.put("image", "/image/0010.jpg");
//        user.put("category", "기타");
//        user.put("user", "하영");
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("history")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("jhy", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("jhy", "Error adding document", e);
//                    }
//                });
//
//        db.collection("history").document("NM5PK8bJ4olGzTy1xDrD")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            Log.d("jhy", document.getId() + " => " + document.getData());
////                            for (QueryDocumentSnapshot document : task.getResult()) {
////                                Log.d("jhy", document.getId() + " => " + document.getData());
////                            }
//                        } else {
//                            Log.w("jhy", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//
//        db.collection("history")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
////                            DocumentSnapshot document = task.getResult();
////                            Log.d("jhy", document.getId() + " => " + document.getData());
////                            for (QueryDocumentSnapshot document : task.getResult()) {
////                                Log.d("jhy", document.getId() + " => " + document.getData());
////                            }
//                        } else {
//                            Log.w("jhy", "Error getting documents.", task.getException());
//                        }
//                    }
//                });

    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                e.getStackTrace();
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {
            String spreadsheetId = "1uUayAOJlYP17jIxuZf3UsQA-sTLSfMcnYcur47ckNaE";
            String range = "거래!B8";
            List<String> results = new ArrayList<String>();

            Object a1 = new Object();
            a1 = "날짜";
            Object b1 = new Object();
            b1 = "금액";
            Object c1 = new Object();
            c1 = "사용처";
            Object d1 = new Object();
            d1 = "내용";
            Object e1 = new Object();
            e1 = "결제방법";
            Object f1 = new Object();
            f1 = "사용자";

            ValueRange valueRange = new ValueRange();
            valueRange.setValues(
                    Arrays.asList(
                            Arrays.asList(a1, b1, c1, d1, e1, f1)));
            UpdateValuesResponse response = this.mService.spreadsheets().values().update(spreadsheetId, range, valueRange)
                    .setValueInputOption("RAW")
                    .execute();
//            ValueRange response = this.mService.spreadsheets().values()
//                    .get(spreadsheetId, range)
//                    .execute();
//            List<List<Object>> values = response.getValues();
//            if (values != null) {
//                results.add("날짜, 금액");
//                for (List row : values) {
//                    results.add(row.get(0) + ", " + row.get(1));
//                }
//            }
            return results;
        }
    }

    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS };
    GoogleAccountCredential mCredential;
    @Override
    protected void onResume() {
        super.onResume();
        startActivity(new Intent(this,MainActivity.class));

//        mCredential = GoogleAccountCredential.usingOAuth2(
//                getApplicationContext(), Arrays.asList(SCOPES))
//                .setBackOff(new ExponentialBackOff());
//        new MakeRequestTask(mCredential).execute();
    }
}
