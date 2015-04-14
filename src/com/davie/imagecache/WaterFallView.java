package com.davie.imagecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.LinkedList;
import java.util.List;

/**
 * User: davie
 * Date: 15-4-7
 */
public class WaterFallView extends ScrollView {

    private LinearLayout waterPool;
    private int columnNum ;//瀑布流的列数
    //保存每一列的linearlayout,方便以后添加图片使用
    private List<LinearLayout> columns;

    private OnLoadMoreListener loadMoreListener;

    public WaterFallView(Context context) {
        this(context,null);
    }

    /**
     * 布局中使用
     * @param context
     * @param attrs
     */
    public WaterFallView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 布局中使用
     * @param context
     * @param attrs
     */
    public WaterFallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs,defStyle);//调用初始化方法
    }

    /**
     * 通用的初始化方法,用于创建瀑布流内部容器.
     */
    private void init(Context context,AttributeSet attrs , int defStyle){
        columnNum = 3;//默认设置为3

        waterPool = new LinearLayout(context);
        waterPool.setOrientation(LinearLayout.HORIZONTAL);//设置水平布局

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        waterPool.setLayoutParams(lp);

        //设置背景颜色
        waterPool.setBackgroundColor(Color.GREEN);

        //-----------------------
        columns  = new LinkedList<LinearLayout>();
        //添加列
        for (int i = 0; i < columnNum; i++) {
            //创建列
            LinearLayout column = new LinearLayout(context);
            column.setOrientation(LinearLayout.VERTICAL);
            //设置尺寸
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            );
            switch (i){
                case 0:
                    column.setBackgroundColor(Color.RED);
                    break;
                case 1:
                    column.setBackgroundColor(Color.BLUE);
                    break;
                case 2:
                    column.setBackgroundColor(Color.YELLOW);
                    break;
            }

            column.setLayoutParams(params);
            //保存列的引用
            columns.add(column);

            waterPool.addView(column);
        }
        //-----------------------

        //添加子控件
        addView(waterPool);
    }

    //图片添加的接口
    private int imageCount ;
    /**
     * 添加图片的接口
     * @param bitmap
     */
    public void addImage(Bitmap bitmap){
        if (bitmap != null) {
            //创建Imageview,设置图片,添加到column当中
            ImageView imageview = new ImageView(getContext());
            imageview.setImageBitmap(bitmap);
            ViewGroup.LayoutParams lp
                    = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            imageview.setLayoutParams(lp);
            imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //根据当前添加了几个来进行第几行的设置
            LinearLayout linearLayout = columns.get(imageCount % columnNum);
            linearLayout.addView(imageview);
            imageCount++;
        }
    }

    //事件处理

    public OnLoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }



    /**
     *
     * 如果返回true,就代表这个事件被消费了.父容器不在进行处理
     * 返回false,父容器可以继续进行处理
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean bret = false;
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                //强制要求,继续接收其余的事件
                bret = true;
                break;
            case MotionEvent.ACTION_UP:
                //当手抬起来的时候,检查是否到底部了
                //1.获取当前ScrollView内容的顶部,滚出去多少
                int scrollY = getScrollY();
                //2.获取ScrollYView的高度
                int height = getHeight();
                //3.内部内容的总高度
                int measuredHeight = waterPool.getMeasuredHeight();
                if(scrollY + height >= measuredHeight){
                    //TODO 已经移动到最底边了
                    if(loadMoreListener !=null){
                        loadMoreListener.onBottom();
                    }
                }else if(scrollY == 0){
                    if(loadMoreListener!=null){
                        loadMoreListener.onTop();
                    }
                }
                break;
            default:
                bret = super.onTouchEvent(event);
                break;
        }
        return bret;
    }

    public interface OnLoadMoreListener{
        /**
         * 到最顶部了
         */
        void onTop();

        /**
         * 到最底部了
         */
        void onBottom();
    }
}
