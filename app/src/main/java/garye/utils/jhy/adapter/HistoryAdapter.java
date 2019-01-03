package garye.utils.jhy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import garye.utils.jhy.R;
import garye.utils.jhy.common.JUtil;
import garye.utils.jhy.data.MainData;

/**
 * Created by hayoung on 31/12/2018.
 * gkduduu@naver.com
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<MainData> items;
    int item_layout;

    public HistoryAdapter(Context context, List<MainData> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MainData item = items.get(position);
        if("지출".equals(item.getState())) {
            holder.hisState.setBackgroundColor(Color.parseColor("#FF5A5A"));
        } else if("수입".equals(item.getState())) {
            holder.hisState.setBackgroundColor(Color.parseColor("#5B5AFF"));
        }
        holder.hisShop.setText(item.getStore());
        holder.hisMoney.setText(item.getMoney());
        holder.hisCard.setText(item.getCard());
        holder.hisCategory.setText(item.getCategory());
        holder.hisUser.setText(item.getUser());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView hisCell;
        View hisState;
        TextView hisDate;
        TextView hisShop;
        TextView hisMoney;
        TextView hisCard;
        TextView hisCategory;
        TextView hisUser;

        public ViewHolder(View itemView) {
            super(itemView);
            hisCell = itemView.findViewById(R.id.HIS_CELL);
            hisState = itemView.findViewById(R.id.HIS_STATE);
            hisDate = itemView.findViewById(R.id.HIS_DATE);
            hisShop = itemView.findViewById(R.id.HIS_SHOP);
            hisMoney = itemView.findViewById(R.id.HIS_MONEY);
            hisCard = itemView.findViewById(R.id.HIS_CARD);
            hisCategory = itemView.findViewById(R.id.HIS_CATEGORY);
            hisUser = itemView.findViewById(R.id.HIS_USER);
        }
    }

}
