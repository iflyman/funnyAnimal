package com.funnyAnimal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.funnyAnimal.R;
import com.funnyAnimal.adapter.GetCityAdapter;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.UserInfo;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class GetCityActivity extends SwipeBackActivity implements OnItemClickListener {
    @BindView(R.id.city_list)
    ListView cityList;

    @BindView(R.id.tv_provice)
    TextView tvProvice;

    private MainApi mainApi;
    private String provice;
    private ArrayList<String> list;
    private GetCityAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.act_get_city;
    }

    @Override
    protected void afterViews() {
        Bundle bundle = getIntent().getExtras();
        provice = bundle.getString("provice");
        list = bundle.getStringArrayList("city");
        tvProvice.setText("省份:" + provice);
        adapter = new GetCityAdapter(getApplicationContext(), bundle.getStringArrayList("city"));
        cityList.setAdapter(adapter);
        cityList.setOnItemClickListener(this);
        mainApi = RetrofitUtils.get().create(MainApi.class);
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
        String cityName = list.get(position);
        toCommitLocation(provice, cityName);
    }

    private void toCommitLocation(final String provice, final String cityName) {
        showProgressDialog("正在修改...");
        ArrayMap<String, String> params = new ArrayMap<>(3);
        params.put("province", provice);
        params.put("city", cityName);
        params.put("accessToken", PreferenceUtil.getUserUuid());
        addSubscriptions(mainApi.userModify(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<UserInfo>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onNext(Result<UserInfo> result) {
                        if (result.code == 0) {
                            Intent intent2 = new Intent(GetCityActivity.this, ModifyUserInfoActivity.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("provice", provice);
                            bundle2.putString("cityName", cityName);
                            intent2.putExtras(bundle2);
                            setResult(RESULT_OK, intent2);
                            PreferenceUtil.setUserProvince(provice);
                            PreferenceUtil.setUserCity(cityName);
                            finish();
                        } else {
                            UiHelper.showToast(GetCityActivity.this, result.message);
                        }
                    }
                }));
    }
}
