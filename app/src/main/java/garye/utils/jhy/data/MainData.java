package garye.utils.jhy.data;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by hayoung on 27/12/2018.
 * gkduduu@naver.com
 */
public class MainData extends RealmObject implements Serializable {
    int seq;
    Date date;
    String shop;
    String state;
    long money;
    String card;
    String comment;
    String image;
    String category;
}
