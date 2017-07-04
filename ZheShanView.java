package com.shengshixuanhe.calligraphycreation.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shengshixuanhe.calligraphycreation.R;
import com.shengshixuanhe.calligraphycreation.bean.PieItem;
import com.shengshixuanhe.calligraphycreation.widget.Constant;

import java.util.ArrayList;
import java.util.List;

import static com.shengshixuanhe.calligraphycreation.utils.MathUtils.angle2arc;

/**
 *
 * Created by 弗莱夏尔 on 2017/5/18.
 */

public class ZheShanView extends FrameLayout{

    private Context context;
    private View childView;

    private Point mCenter;//中心点
    private int mInnerRadius;//内圆半径
    private int mRadiusInc;//扇形半径

    private List<List<PieItem>> mItemList;
    private static final int MAX_LEVEL = 2;

    private Paint zheshan;
    int mItemSize;
    private boolean mIsLayoutItems = false;
    private static final int ELLIPSE_ANGLE = 130;//折扇角度

    public ZheShanView(Context context) {
        super(context);
        this.context = context;

        initView();
    }

    private void initView() {
        mItemList = new ArrayList<List<PieItem>>();

        mItemSize = (int) context.getResources().getDimension(R.dimen.pie_item_size);
        mInnerRadius = Constant.DEFAULT_INNER_RADIUS;
        mRadiusInc = Constant.DEFAULT_OUTER_RADIUS;
        setWillNotDraw(false);
        setDrawingCacheEnabled(false);

        zheshan = new Paint();
        zheshan.setAntiAlias(true);
        zheshan.setColor(Color.WHITE);
    }
    
    public static double angle2arc(int angle) {
        return (angle * Math.PI / 180);
    }

    public static int arc2angle(double arc) {
        return (int) (arc * 180 / Math.PI);
    }

    public void setCenter(Point center) {
        mCenter = center;
    }

    public void setCenter(int x, int y) {
        if (mCenter == null) {
            mCenter = new Point(x, y);
        } else {
            mCenter.x = x;
            mCenter.y = y;
        }
    }

    public void addItem(PieItem item) {
        int level = item.getLevel();
        if (level >= MAX_LEVEL) {
            level = MAX_LEVEL - 1;
        }
        while (mItemList.size() <= level) {
            mItemList.add(new ArrayList<PieItem>());
        }
        mItemList.get(level).add(item);
    }

    /**
     *给item布局
     */
    private void layoutItems() {
        int inner = mInnerRadius;
        int outer = mInnerRadius + mRadiusInc;
        int incInterval = 2;
        int startAngleInterval = 45;
        int levelStartAngle = 45;
        int i = 0;
        int j = 0;

        for (List<PieItem> list : mItemList) {
            ++j;
            int itemAngle = ELLIPSE_ANGLE / list.size();
            int startAngle = 205;//扇形起始角度
            for (PieItem item : list) {
                i++;
                View view = item.getView();
                view.measure(view.getLayoutParams().width,view.getLayoutParams().height);
                int w = view.getMeasuredWidth();
                int h = view.getMeasuredHeight();
                int r = 0;
                if(list.size() * mItemList.size() > 7 && j == 1){//内部item
                    outer =  mInnerRadius + mRadiusInc / 2;
                    r = inner + (outer - inner) / 4 * 3;
                }else if(list.size() * mItemList.size() > 7 && j == 2){//第二圈
                    outer =  mInnerRadius + mRadiusInc / 2;
                    r = inner + (outer - inner) / 2;
                }else {
                    //show in center
                    outer = mInnerRadius + mRadiusInc;
                    r = inner + (outer - inner) / 2;
                }
                Log.i("item半径",r + "");
                double arc = angle2arc(startAngle + itemAngle / 2);
                int x = mCenter.x + (int) (r * Math.cos(arc)) - w / 2;
                int y = mCenter.y + (int) (r * Math.sin(arc)) - h / 2;
                view.layout(x, y, x + w, y + h);
                Path path = null;
                path = makePath(startAngle, startAngle + itemAngle, inner + outer, outer, mCenter);
                item.setGeometry(startAngle, itemAngle, inner + outer, outer, path);
                startAngle += itemAngle;
            }
            if(i == list.size() && j == 2) {//第二圈
                inner = mRadiusInc;
                outer = mInnerRadius + mRadiusInc / 2;
                levelStartAngle = 45;
            }else {
                inner += mRadiusInc + incInterval;
                outer += mRadiusInc + incInterval;
                levelStartAngle += startAngleInterval;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!mIsLayoutItems && mItemList.size() != 0){
            layoutItems();
            mIsLayoutItems = true;
        }
        //画折扇
        canvas.drawPath(makePath(205,335,mInnerRadius,mInnerRadius + mRadiusInc,mCenter),zheshan);
        int state;
        for (List<PieItem> list : mItemList){
            for (PieItem item : list) {
                state = canvas.save();
//                drawPath(canvas, item.getPath(), paint);
                canvas.restoreToCount(state);
                drawItem(canvas, item);
            }
        }
    }

    private void drawPath(Canvas canvas, Path path, Paint paint) {
        canvas.drawPath(path, paint);
    }

    /**
     * 确定item位置
     */
    private Path makePath(int startAngle, int endAngle, int inner, int outer, Point center) {
        Log.i("扇形起始点坐标", "start, end = " + startAngle + ", " + endAngle);
        Path path = new Path();
        RectF innerRect = new RectF(center.x - inner, center.y - inner,
                center.x + inner, center.y + inner);
        RectF outerRect = new RectF(center.x - outer, center.y - outer,
                center.x + outer, center.y + outer);
        path.arcTo(innerRect, startAngle, endAngle - startAngle, true);
        path.arcTo(outerRect, endAngle, startAngle - endAngle, false);
        path.close();
        return path;
    }

    private void drawItem(Canvas canvas, PieItem item) {
        View view = item.getView();
        int state = canvas.save();
        canvas.translate(view.getX(), view.getY());
        view.draw(canvas);
        canvas.restoreToCount(state);
        state = canvas.save();
        canvas.restoreToCount(state);
    }

    public PieItem makeItem(ImageView view, int level) {
        view.setMinimumWidth(mItemSize);
        view.setMinimumHeight(mItemSize);
        view.setScaleType(ImageView.ScaleType.CENTER);
        LayoutParams lp = new LayoutParams(mItemSize, mItemSize);
        view.setLayoutParams(lp);
        return new PieItem(view, level);
    }
}
