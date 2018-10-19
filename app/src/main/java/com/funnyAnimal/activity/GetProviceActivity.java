package com.funnyAnimal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.funnyAnimal.R;
import com.funnyAnimal.adapter.GetProviceAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class GetProviceActivity extends SwipeBackActivity implements OnItemClickListener {

    @BindView(R.id.provice_list)
    ListView proviceList;

    private GetProviceAdapter adapter;
    private List<String> mProvices = new ArrayList<>();
    private Map<String, List<String>> mCityMap = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.act_get_provice;
    }

    @Override
    protected void afterViews() {
        initJsonDatas();
        adapter = new GetProviceAdapter(getApplicationContext(), mProvices);
        proviceList.setAdapter(adapter);
        proviceList.setOnItemClickListener(this);
    }

    private void initJsonDatas() {
        JSONArray jsonArray;
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "gbk"));
            }
            is.close();
            jsonArray = new JSONObject(sb.toString()).getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = (JSONObject) jsonArray.get(i);
                String proviceJson = jsonP.getString("name");
                mProvices.add(proviceJson);
                String city = jsonP.getString("cities");
                city = city.replaceAll("\"", "");
                city = city.replace("[", "");
                city = city.replace("]", "");
                mCityMap.put(proviceJson, Arrays.asList(city.split(",")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.back_btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String provice = mProvices.get(position);
        ArrayList<String> cityList = new ArrayList<>();
        for (int i = 0 ; i <mCityMap.get(provice).size();i++) {
            cityList.add(mCityMap.get(provice).get(i));
        }
        Intent intent = new Intent(this, GetCityActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("provice", provice);
        bundle.putStringArrayList("city", cityList);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String provice = bundle.getString("provice");
            String cityName = bundle.getString("cityName");
            Intent intentSet = new Intent(this, ModifyUserInfoActivity.class);
            Bundle bundleSet = new Bundle();
            bundleSet.putString("provice", provice);
            bundleSet.putString("cityName", cityName);
            intentSet.putExtras(bundleSet);
            setResult(RESULT_OK, intentSet);
            finish();
        }
    }
}
