package garye.utils.jhy;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.ArrayList;

import garye.utils.jhy.adapter.HistoryAdapter;
import garye.utils.jhy.common.JConst;
import garye.utils.jhy.data.MainData;
import garye.utils.jhy.sheet.SheetUtils;

public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressBar PROGRESS;
    private ArrayList<MainData> mDataList = new ArrayList<>();

    public ListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        new MakeRequestTask().execute();
        super.onCreate(savedInstanceState);
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.sheets.v4.Sheets mService = null;

        MakeRequestTask() {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, JConst.getInstance().Credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                mDataList = SheetUtils.getGagyeData(mService);
//                return SheetUtils.getGagyeData(mService);
            } catch (UserRecoverableAuthIOException e) {
                Log.i("jhy","doInBackground!"  + e.getMessage());
                startActivityForResult(e.getIntent(), JConst.REQUEST_AUTHORIZATION);
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }finally {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            getActivity().findViewById(R.id.SET_PROGRESS).setVisibility(View.GONE);

            mRecyclerView = getActivity().findViewById(R.id.SET_RECYCLER);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setAdapter(new HistoryAdapter(getContext(), mDataList));
        }
    }
}
