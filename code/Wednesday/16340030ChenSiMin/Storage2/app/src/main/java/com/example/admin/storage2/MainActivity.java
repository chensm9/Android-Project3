package com.example.admin.storage2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private Bitmap bitmap = null;
    private myDB database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRadioCheckedChanged();
        database = myDB.getInstance(this);
    }

    private void setRadioCheckedChanged() {
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        // 设置监听单选按钮组变化
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                if (rb.getText().toString().equals("Login")) {
                    findViewById(R.id.password).setVisibility(View.VISIBLE);
                    findViewById(R.id.newPassword).setVisibility(View.GONE);
                    findViewById(R.id.confirmPassword).setVisibility(View.GONE);
                    findViewById(R.id.image).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.password).setVisibility(View.GONE);
                    findViewById(R.id.newPassword).setVisibility(View.VISIBLE);
                    findViewById(R.id.confirmPassword).setVisibility(View.VISIBLE);
                    findViewById(R.id.image).setVisibility(View.VISIBLE);
                    CLEARButtonOnClick(null);
                }
            }
        });
    }

    public void CLEARButtonOnClick(View target) {
        ((EditText)findViewById(R.id.newPassword)).setText("");
        ((EditText)findViewById(R.id.confirmPassword)).setText("");
        ((EditText)findViewById(R.id.password)).setText("");
        ((EditText)findViewById(R.id.username)).setText("");
        ((ImageView)findViewById(R.id.image)).setImageResource(R.mipmap.add);
        bitmap = null;
    }

    public void OKButtonOnClick(View target) {
        if (findViewById(R.id.password).getVisibility() == View.VISIBLE) {
            // Login状态
            String username = ((EditText)findViewById(R.id.username)).getText().toString();
            if (username.equals("")) {
                Toast.makeText(this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            String password = ((EditText)findViewById(R.id.password)).getText().toString();
            if (password.equals("")) {
                Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!database.queryUserByName(username)) {
                Toast.makeText(this, "Username not existed.", Toast.LENGTH_SHORT).show();
                return;
            }
            UserInfo data = database.getUserByName(username);
            if (!data.getPassword().equals(password)){
                Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Correct Password.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, CommentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            // Registes状态
            String username = ((EditText)findViewById(R.id.username)).getText().toString();
            if (username.equals("")) {
                Toast.makeText(this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            String newPassword = ((EditText)findViewById(R.id.newPassword)).getText().toString();
            if (newPassword.equals("")) {
                Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            String confirmPassword = ((EditText)findViewById(R.id.confirmPassword)).getText().toString();
            if (!confirmPassword.equals(newPassword)) {
                Toast.makeText(this, "Password Mismatch.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (database.queryUserByName(username)) {
                Toast.makeText(this, "Username already existed.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.me);
            }
            UserInfo data = new UserInfo();
            data.setName(username);
            data.setPassword(newPassword);
            data.setImage(bitmap);
            database.insertUser(data);

            // 注册成功不进行跳转，弹出toast提示
            Toast.makeText(this, "Register Successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    public void ImageViewOnClick(View target) {
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //如果有权限直接执行
            LoadImage();
        } else{
            //如果没有权限那么申请权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }

    void LoadImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            // 得到图片的全路径
            Uri uri = data.getData();
            try {
                // 通过路径加载图片
                // 同时图片缩放操作，如果图片过大，可能会导致内存泄漏
                bitmap = getBitmapFormUri(uri);
                ImageView iv_image = findViewById(R.id.image);
                iv_image.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, "图片读取错误", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                LoadImage();
            }else{
                Toast.makeText(this, "您已经拒绝读取照片的权限", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    public Bitmap getBitmapFormUri(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以120x120为标准
        float hh = 120f;  //这里设置高度为120f
        float ww = 120f;  //这里设置宽度为120f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;  //be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {
            //如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {
            //如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
