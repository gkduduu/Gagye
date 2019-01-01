package garye.utils.jhy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import garye.utils.jhy.adapter.HistoryAdapter;
import garye.utils.jhy.data.MainData;

public class ListActivity extends BaseActivity {

    RecyclerView historyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        historyView = findViewById(R.id.HIS_RECYCLER);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        historyView.setHasFixedSize(true);
        historyView.setNestedScrollingEnabled(false);
        historyView.setLayoutManager(layoutManager);


        List<MainData> data = new ArrayList<>();
        data.add(new MainData("",Calendar.getInstance().getTime(),"HY게스트하우스","지출","10,000원","크마카드","뿌잉","/image/0010.jpg","숙박","하영"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"로또1등 당첨","수입","2,200,000,000원","하나통장","댑악","/image/0010.jpg","기타","하영"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"토횬떡뽁이","지출","900,000원","크마카드","존맛","/image/0010.jpg","식사","하영"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"e마트","지출","46,000원","토횬우리","뿌잉","/image/0010.jpg","숙박","토횬"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"힐튼 후쿠오카 씨오크 30박","지출","6,000,000원","크마카드","뿌잉","/image/0010.jpg","숙박","하영"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"e마트","지출","46,000원","토횬우리","뿌잉","/image/0010.jpg","숙박","토횬"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"e마트","지출","46,000원","토횬우리","뿌잉","/image/0010.jpg","숙박","토횬"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"e마트","지출","46,000원","토횬우리","뿌잉","/image/0010.jpg","숙박","토횬"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"e마트","지출","46,000원","토횬우리","뿌잉","/image/0010.jpg","숙박","토횬"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"e마트","지출","46,000원","토횬우리","뿌잉","/image/0010.jpg","숙박","토횬"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"e마트","지출","46,000원","토횬우리","뿌잉","/image/0010.jpg","숙박","토횬"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"e마트","지출","46,000원","토횬우리","뿌잉","/image/0010.jpg","숙박","토횬"));
        data.add(new MainData("",Calendar.getInstance().getTime(),"e마트","지출","46,000원","토횬우리","뿌잉","/image/0010.jpg","숙박","토횬"));

        historyView.setAdapter(new HistoryAdapter(getApplicationContext(), data, R.layout.activity_list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
