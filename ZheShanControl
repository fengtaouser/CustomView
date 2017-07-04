package com.shengshixuanhe.calligraphycreation.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shengshixuanhe.calligraphycreation.R;
import com.shengshixuanhe.calligraphycreation.interfaces.From;
import com.shengshixuanhe.calligraphycreation.utils.ToastUtil;
import com.shengshixuanhe.calligraphycreation.widget.Constant;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * Created by 弗莱夏尔 on 2017/5/18.
 */

public class ZheShan implements From{

    private Context context;
    private ArrayList<ImageView> rotatelist;
    private LinearLayout ll;
    private ZheShanView zsv;
    private int itemAngle;
    private static final int ELLIPSE_ANGLE = 130;

    public ZheShan(Context context, LinearLayout ll,ArrayList<ImageView> list) {
        this.context = context;
        this.ll = ll;
        rotatelist = new ArrayList<ImageView>();
        for(int i = 0;i < list.size();i++){
            View view = View.inflate(context,R.layout.item_word,null);
            ImageView iv = (ImageView) view.findViewById(R.id.item_iv_word);
            iv.setImageDrawable(list.get(i).getDrawable());
            rotatelist.add(iv);
        }
        Collections.reverse(rotatelist);
        intitView(rotatelist);
    }

    private void intitView(ArrayList<ImageView> list) {
        zsv = new ZheShanView(context);
        if(list.size() < 8){
            itemAngle = ELLIPSE_ANGLE / list.size();
        }else {
            itemAngle = ELLIPSE_ANGLE / (list.size() / 2);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        zsv.setCenter(dm.widthPixels / 2, dm.heightPixels / 2 + 110);
        zsv.setCenter(ll.getWidth() / 2,ll.getHeight()/2 + Constant.DEFAULT_OUTER_RADIUS/2);
        Log.i("折扇圆心点坐标","X=" + ll.getWidth() / 2 + "  Y=" + ll.getHeight()/2 + Constant.DEFAULT_OUTER_RADIUS/2);
        zsv.setLayoutParams(lp);

        for(int i = 0;i < list.size();i++){
            ivRotate(itemAngle, i, list.get(i));
            if(i < list.size() / 2 && list.size() > 7){//靠近圆心的字
                zsv.addItem(zsv.makeItem(list.get(i),0));
            }else if(i >= list.size() / 2 && list.size() > 7){//靠外部的字
                zsv.addItem(zsv.makeItem(list.get(i),1));
            }else {//只有一行字
                zsv.addItem(zsv.makeItem(list.get(i),0));
            }
        }
    }

    /**
     * 根据位置的不同确定旋转角度
     * @param itemAngle  根据item确定间隔角度
     * @param i  第几个item
     * @param iv
     */
    private void ivRotate(int itemAngle, int i, ImageView iv) {
        BitmapDrawable bd = (BitmapDrawable) iv.getDrawable();
        Bitmap bm = bd.getBitmap();
        Matrix m = new Matrix();
        int degres = 0;
        if(i < rotatelist.size() / 2 && rotatelist.size() > 7){
            degres = 304 + itemAngle * i;
            m.setRotate(degres%360,bm.getWidth(),bm.getHeight());
        }else if(i >= rotatelist.size() / 2 && rotatelist.size() > 7){
            degres = 304 + itemAngle * (i - rotatelist.size() / 2);
            m.setRotate(degres%360,bm.getWidth(),bm.getHeight());
        }else if(rotatelist.size() != 1){
            int start = 295 + (ELLIPSE_ANGLE/rotatelist.size())/2;
            degres = start + itemAngle * i;
            m.setRotate(degres%360,bm.getWidth(),bm.getHeight());
        }
        Log.i("旋转角度======",degres%360 + "");
        int width = bm.getWidth();
        int height = bm.getHeight();
        Bitmap b = Bitmap.createBitmap(bm,0,0,width,height,m,true);
        iv.clearColorFilter();
        iv.setBackgroundColor(Color.TRANSPARENT);
        iv.setImageBitmap(b);
    }

    @Override
    public View addFrom() {
        ll.removeAllViews();
        if(rotatelist.size() < 15 && rotatelist.size() != 11 && rotatelist.size() != 13 && rotatelist.size() != 9){
            return zsv;
        }
        ToastUtil.showToast(context,R.string.from_null);
        return new View(context);
    }

}
