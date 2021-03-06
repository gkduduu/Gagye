package garye.utils.jhy.sheet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import garye.utils.jhy.common.JConst;
import garye.utils.jhy.data.MainData;
import garye.utils.jhy.dialog.InputDialog;

/**
 * Created by hayoung on 03/01/2019.
 * gkduduu@naver.com
 */
public class SheetUtils {

    //마지막쓰여진 라인 찾아서 리턴
    private static String getLastIndex(Sheets service) throws Exception {
        String range = "거래!A5";

        ValueRange response = service.spreadsheets().values()
                .get(JConst.SPREADSHEET_ID, range)
                .execute();
        String lastIndex = "";
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {
                lastIndex = (Integer.parseInt(row.get(0).toString().replace("C","")) + 4) + "";
                break;
            }
        }
        Log.i("jhy", "lastindex ===> " +lastIndex);
        return lastIndex;
    }

    //카테고리 불러오기
    public static String getOUTCategory(Sheets service, InputDialog that) throws Exception {
        String range = "요약!B28:B35";
        ValueRange response = service.spreadsheets().values()
                .get(JConst.SPREADSHEET_ID, range)
                .execute();
        String lastIndex = "";
        List<List<Object>> values = response.getValues();
        if (values != null) {
            int i = 0;
            for (List row : values) {
                Log.i("jhy",row.get(0).toString());
                that.arrCate[i] = row.get(0).toString();
                i = i+1;
            }
        }
        return "CATE";
    }

    public static String putData(Sheets service, MainData data) throws Exception {
        String range = "거래!B" + getLastIndex(service);
        List<String> results = new ArrayList<String>();

        Object a1 = new Object();
//        a1 = new SimpleDateFormat("yyyy-MM-dd").parse(data.getDate());
        a1 = data.getDate();
        Object b1 = new Object();
        b1 = Integer.parseInt(data.getMoney());
        Object c1 = new Object();
        c1 = data.getStore();
        Object d1 = new Object();
        d1 = data.getComment();
        Object e1 = new Object();
        e1 = data.getCard();
        Object f1 = new Object();
        f1 = data.getUser();
        Object g1 = new Object();
        g1 = data.getCategory();

        ValueRange valueRange = new ValueRange();
        valueRange.setValues(
                Arrays.asList(
                        Arrays.asList(a1, b1, c1, d1, e1, f1, g1)));
        UpdateValuesResponse response = service.spreadsheets().values().update(JConst.SPREADSHEET_ID, range, valueRange)
                .setValueInputOption("RAW")
                .execute();

        return "OK";
    }

    //해당 시트의 정보 얻어오기
    public static ArrayList<MainData> getGagyeData(Sheets service) throws Exception {
        ArrayList<MainData> list = new ArrayList<>();
        String range = "거래!B5:H";

        ValueRange response = service.spreadsheets().values()
                .get(JConst.SPREADSHEET_ID, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {
                //날짜    금액  사용처 내용  카드  사용자 카테고리
                MainData data = new MainData();
                data.setDate(row.get(0).toString());
                data.setMoney(row.get(1).toString());
                data.setStore(row.get(2).toString());
                data.setComment(row.get(3).toString());
                data.setCard(row.get(4).toString());
                data.setUser(row.get(5).toString());
                data.setCategory(row.get(6).toString());
                list.add(data);
            }
        }
        return list;
    }


    //온라인 확인
    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    //스토어 활성화 확인
    public static boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }
    public static void acquireGooglePlayServices(Activity context) {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode,context);
        }
    }
    private static void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode, final Activity context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                context,
                connectionStatusCode,
                JConst.REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

}
