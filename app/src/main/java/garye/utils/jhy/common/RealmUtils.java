package garye.utils.jhy.common;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import garye.utils.jhy.data.CardData;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by hayoung on 2018. 9. 6..
 * gkduduu@naver.com
 */
public class RealmUtils {

    private static Realm set(Context context) {
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("jhy.gagye")
                .schemaVersion(JConst.REALM_VERSION)
                .build();

        return Realm.getInstance(config);
    }

    public static void addCard(Context context,final String cardname) {
        Realm realm = set(context);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObject(CardData.class,cardname);
            }
        });
        realm.close();
    }

    public static List<CardData> queryAll(Context context) {
        Realm realm = set(context);

        RealmResults<CardData> result = realm.where(CardData.class).findAll();


        if(result.size() == 0) {
            return null;
        }
        realm.close();

        return result;
    }
}
