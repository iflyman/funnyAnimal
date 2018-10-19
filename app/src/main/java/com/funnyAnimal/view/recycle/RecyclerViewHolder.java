package com.funnyAnimal.view.recycle;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnyAnimal.view.CircleImageView;
import com.funnyAnimal.view.MLImageView;

/**
 * RecyclerView's common Adapter
 *
 * @author brucewuu Created on 16/4/27.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private final SparseArrayCompat<View> viewSparseArray;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.viewSparseArray = new SparseArrayCompat<>();
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"}) // Checked by runtime cast. Public API.
    public <V extends View> V getView(int resId) {
        View view = viewSparseArray.get(resId);
        if (null == view) {
            view = itemView.findViewById(resId);
            viewSparseArray.put(resId, view);
        }
        return (V) view;
    }

    public TextView getText(int resId) {
        return getView(resId);
    }

    public ImageView getImageView(int resId){
        return getView(resId);
    }
    public Button getButton(int resId) {
        return getView(resId);
    }

    public AppCompatButton getAppCompatButton(int resId) {
        return getView(resId);
    }

    public RelativeLayout getRelativeLayout(int resId) {
        return getView(resId);
    }

    public LinearLayout getLinearLayout(int resId) {
        return getView(resId);
    }

    public GridView getGridView(int resId) {
        return getView(resId);
    }

    public CircleImageView getCircleImageView(int resId) {
        return getView(resId);
    }
}
