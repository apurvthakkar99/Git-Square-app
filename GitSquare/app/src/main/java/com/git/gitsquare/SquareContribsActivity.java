package com.git.gitsquare;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.git.gitsquare.adapters.UserDetailAdapter;
import com.git.gitsquare.application.AppController;
import com.git.gitsquare.model.UserDetail;
import com.git.gitsquare.utils.Validations;
import com.git.gitsquare.utils.Webservices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SquareContribsActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvUserDetail;
    private TextView tvFilterContribs;
    private LinearLayout layProgressBar;
    private ArrayList<UserDetail> arrUserDeatil;
    UserDetailAdapter userDetailAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isFiltered;

    String  tag_json_arry = "json_array_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_contribs);

        findViews();
        setOnClickListener();

        if (!Validations.checkInternetConnection(SquareContribsActivity.this)){
            Toast.makeText(getApplicationContext(),R.string.no_internet,Toast.LENGTH_SHORT).show();
        }else {
            layProgressBar.setVisibility(View.VISIBLE);
            getUserDetailWebservice();
        }
    }

    private void findViews() {

        lvUserDetail = (ListView) findViewById(R.id.lvUserDetail);
        tvFilterContribs= (TextView) findViewById(R.id.tvFilterContribs);
        layProgressBar = (LinearLayout) findViewById(R.id.layProgressBar);
        arrUserDeatil = new ArrayList<UserDetail>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!Validations.checkInternetConnection(SquareContribsActivity.this)){
                    Toast.makeText(getApplicationContext(),R.string.no_internet,Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }else {
                    //layProgressBar.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    getUserDetailWebservice();
                }
            }
        });
    }

    private void setOnClickListener() {
        tvFilterContribs.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getUserDetailWebservice() {

        JsonArrayRequest strReq = new JsonArrayRequest(Webservices.GET_USER_DETAIL,
                new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                parseResponse(response);
                mSwipeRefreshLayout.setRefreshing(false);
                layProgressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                layProgressBar.setVisibility(View.GONE);
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_arry);
    }

    private void parseResponse(JSONArray response) {

        if(arrUserDeatil.size()>0){
            arrUserDeatil.clear();
        }

        try {
            for (int i = 0 ; i < response.length() ; i++){
                JSONObject jObjUserDetail = response.getJSONObject(i);
                UserDetail userDetail = new UserDetail(jObjUserDetail);
                arrUserDeatil.add(userDetail);
            }

            setAdapter();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setAdapter() {
        if(isFiltered){
            filterContribs();
        }else {
            userDetailAdapter = new UserDetailAdapter(SquareContribsActivity.this, arrUserDeatil);
            lvUserDetail.setAdapter(userDetailAdapter);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvFilterContribs:
                isFiltered = true;
                filterContribs();
                break;
            default:
                break;
        }
    }

    private void filterContribs() {
        Collections.sort(arrUserDeatil, new Comparator<UserDetail>() {
            @Override
            public int compare(UserDetail o1, UserDetail o2) {
                return Integer.valueOf(o1.getContributions()).compareTo(o2.getContributions());
            }
        });
        userDetailAdapter.notifyDataSetChanged();
    }
}
