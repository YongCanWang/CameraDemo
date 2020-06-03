package cn.mapscloud.www.camerademo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private        String TAG               = this.getClass().getSimpleName();
    private static int    REQUEST_THUMBNAIL = 1;// 请求缩略图信号标识
    private static int    REQUEST_ORIGINAL  = 2;// 请求原图信号标识
    private        Button button1, button2;
    private ImageView mImageView;
    private String    sdPath;//SD卡的路径
    private String    picPath;//图片存储路径
    private Button    bt_camera_breviary;
    private Button    bt_camera_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_camera_breviary = (Button) findViewById(R.id.bt_camera_breviary);
        bt_camera_total = (Button) findViewById(R.id.bt_camera_total);

        bt_camera_breviary.setOnClickListener(this);
        bt_camera_total.setOnClickListener(this);
        sdPath = Environment.getExternalStorageDirectory().getPath();
        picPath = sdPath + "/" + "temp.png";

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_camera_breviary:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 启动相机
                startActivityForResult(intent1, REQUEST_THUMBNAIL);
                break;

            case R.id.bt_camera_total:
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = Uri.fromFile(new File(picPath));
                //为拍摄的图片指定一个存储的路径
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent2, REQUEST_ORIGINAL);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_THUMBNAIL) {
                /**
                          * 通过这种方法取出的拍摄会默认压缩，因为如果相机的像素比较高拍摄出来的图会比较高清，
                          * 如果图太大会造成内存溢出（OOM），因此此种方法会默认给图片尽心压缩
                          */
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                mImageView.setImageBitmap(bitmap);

            } else if (resultCode == REQUEST_ORIGINAL) {//对应第二种方法
                /**
                          * 这种方法是通过内存卡的路径进行读取图片，所以的到的图片是拍摄的原图
                          */
                FileInputStream fis = null;
                try {
                    Log.e("sdPath2", picPath);
                    //把图片转化为字节流
                    fis = new FileInputStream(picPath);
                    //把流转化图片
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    mImageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();//关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
