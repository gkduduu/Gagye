package garye.utils.jhy.data;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by hayoung on 25/01/2019.
 * gkduduu@naver.com
 */
public class CardData extends RealmObject implements Serializable {

    @PrimaryKey
    @Required
    private String CardName;

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }
}
