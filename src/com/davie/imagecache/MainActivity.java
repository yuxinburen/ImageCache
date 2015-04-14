package com.davie.imagecache;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.davie.util.ImageDownloadHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: davie
 * Date: 15-4-7
 */
public class MainActivity extends Activity implements WaterFallView.OnLoadMoreListener {
    private WaterFallView waterFallView;

    private static final String TAG = "MainActivity";

    /*
     * 存储assets目录下的文件
     */
    private List<String> fileNames;
    /*
     *当前页码
     */
    private int currentPage ;

    /**
     * 一页的个数
     */
    public static final int NUM_PER_PAGE = 9;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        waterFallView = (WaterFallView) findViewById(R.id.waterfallview);

        fileNames = new ArrayList<String>();

        waterFallView.setLoadMoreListener(this);
        //获取assets目录下,指定子目录的文件列表

        try {
            InputStream inputStream = getAssets().open("imageurl.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line ;
            while ((line=reader.readLine())!=null){
                fileNames.add(line);
            }
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImages();
    }

    //图片加载的实际方法
    private void loadImages(){
        int startIndex = currentPage * NUM_PER_PAGE;
        int endIndex = startIndex + 9;
        if (startIndex < fileNames.size()) {
            if(endIndex > fileNames.size()) {
                endIndex = fileNames.size() - 1;
            }
            //获取当前集合中,指定的一段内容,生成一个新的集合
            List<String> list = fileNames.subList(startIndex, endIndex);
            //进行图片的加载
            //TODO进行图片的加载
            for (String str : list)
                try {
                    ImageDownloadHelper helper = new ImageDownloadHelper(this);
//                    Bitmap bitmap = helper.getImage(str);
                    helper.getImage(str, new ImageDownloadHelper.OnLoadResultListener() {
                        @Override
                        public void onLoadResult(Bitmap bitmap) {
                            waterFallView.addImage(bitmap);
                        }
                    });
//                    Log.i("MainActivity", bitmap.getWidth() + "," + bitmap.getHeight());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        //增加页码
        currentPage++;
    }

    @Override
    public void onTop() {

    }

    @Override
    public void onBottom() {
        loadImages();
    }
}