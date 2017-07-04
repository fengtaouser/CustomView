package com.shengshixuanhe.calligraphycreation.bean;

import android.graphics.Path;
import android.graphics.PointF;
import android.widget.ImageView;

/**
 *  折扇上的imageview
 * Created by 弗莱夏尔 on 2017/5/19.
 */

public class PieItem {

    private ImageView mView;
    private int mLevel;
    private int mStart;
    private int mSweep;
    private int mInner;
    private int mOuter;
    private boolean mPressed;//是否被选中
    private Path mPath;
    private PointF mCenter = null;

    public PieItem(ImageView view, int level) {
        mView = view;
        mLevel = level;
    }

    public PieItem(float X,float Y){

    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level){
        mLevel = level;
    }

    public void setCenter(PointF center){
        mCenter = center;
    }

    public PointF getCenter(){
        return mCenter;
    }

    public void setGeometry(int start, int sweep, int inside, int outside, Path p) {
        mStart = start;
        mSweep = sweep;
        mInner = inside;
        mOuter = outside;
        mPath = p;
    }

    public int getStartAngle() {
        return mStart;
    }

    public int getSweep() {
        return mSweep;
    }

    public int getInnerRadius() {
        return mInner;
    }

    public int getOuterRadius() {
        return mOuter;
    }

    public ImageView getView() {
        return mView;
    }

    public Path getPath() {
        return mPath;
    }

}
