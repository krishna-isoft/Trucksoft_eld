package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sab99r
 */
public class Dispatchline_decoder extends RecyclerView.ItemDecoration {
    private int space=0;

    public Dispatchline_decoder(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildAdapterPosition(view) == 0)
            outRect.top = space;

        outRect.bottom = space;
    }
}
