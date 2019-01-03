package garye.utils.jhy.data;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hayoung on 27/12/2018.
 * gkduduu@naver.com
 */
public class MainData extends RealmObject implements Serializable {

    @PrimaryKey
    private String seq;
    private String date;
    private String shop;
    private String state;
    private String money;
    private String card;
    private String comment;
    private String image;
    private String category;
    private String user;

    public MainData() {

    }

    public MainData(String seq,String date, String shop, String state, String money, String card,String comment, String image, String category,String user) {
        setSeq(seq);
        setDate(date);
        setStore(shop);
        setState(state);
        setMoney(money);
        setCard(card);
        setComment(comment);
        setImage(image);
        setCategory(category);
        setUser(user);
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStore() {
        return shop;
    }

    public void setStore(String shop) {
        this.shop = shop;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
