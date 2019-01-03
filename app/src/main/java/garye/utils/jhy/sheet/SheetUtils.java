package garye.utils.jhy.sheet;

import android.util.Log;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import garye.utils.jhy.data.MainData;

/**
 * Created by hayoung on 03/01/2019.
 * gkduduu@naver.com
 */
public class SheetUtils {

    //마지막쓰여진 라인 찾아서 리턴
    private static String getLastIndex(Sheets service) throws Exception {
        String spreadsheetId = "1uUayAOJlYP17jIxuZf3UsQA-sTLSfMcnYcur47ckNaE";
        String range = "거래!A5";

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
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

    public static String putData(Sheets service, MainData data) throws Exception {
        String spreadsheetId = "1uUayAOJlYP17jIxuZf3UsQA-sTLSfMcnYcur47ckNaE";
        String range = "거래!B" + getLastIndex(service);
        List<String> results = new ArrayList<String>();

        Object a1 = new Object();
        a1 = "날짜";
        Object b1 = new Object();
        b1 = data.getMoney();
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
        UpdateValuesResponse response = service.spreadsheets().values().update(spreadsheetId, range, valueRange)
                .setValueInputOption("RAW")
                .execute();

        return "OK";
    }
}
