package com.funnyAnimal.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.funnyAnimal.R;
import com.funnyAnimal.adapter.WrapperAdapter;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bhyan on 2017/9/4.
 */

public class WrapperActivity extends SwipeBackActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.mGridView)
    GridView mGridView;

    private WrapperAdapter wrapperAdapter;
    private List<String> items;

    @Override
    protected int getLayoutId() {
        return R.layout.act_wrapper;
    }

    @Override
    protected void afterViews() {
        items = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            items.add("icon_menu_back" + i);
        }

        wrapperAdapter = new WrapperAdapter(this, items);
        mGridView.setAdapter(wrapperAdapter);
        mGridView.setOnItemClickListener(this);
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
        PreferenceUtil.setWrapperResource(items.get(position));
        UiHelper.showToast(this, "设置成功");
        finish();
    }
}
