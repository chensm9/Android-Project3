package com.example.admin.storage2;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private myDB database;
    private UserInfo user;
    private  List<CommentInfo> commentList = new ArrayList<>();
    final MyAdapter myListViewAdapter = new MyAdapter(CommentActivity.this, commentList);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        database = myDB.getInstance(this);
        // 接收数据
        Bundle bundle = getIntent().getExtras();
        user = database.getUserByName(bundle.getString("username"));
        commentList = database.getCommentList(user.getName());
        Init();
    }

    private void Init() {
        final ListView listView = findViewById(R.id.list_view);
        myListViewAdapter.setUser(user);
        listView.setAdapter(myListViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 处理单击事件
                // 获取电话号码
                if(ContextCompat.checkSelfPermission(CommentActivity.this,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                    //如果没有权限那么申请权限
                    final AlertDialog.Builder dialog2 = new AlertDialog.Builder(CommentActivity.this);
                    dialog2.setMessage("是否给予应用读取联系人权限?");
                    dialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            // 根据包名打开对应的设置界面
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    });
                    dialog2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CommentActivity.this, "您已经拒绝读取通信录的权限", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog2.show();
                    return;
                }
                final AlertDialog.Builder dialog = new AlertDialog.Builder(CommentActivity.this);
                String username = ((TextView)view.findViewById(R.id.username)).getText().toString();
                String message = "Username: " + username;
                try {
                    Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = \"" + username + "\"", null, null);
                    if (cursor.moveToFirst())
                        message += "\nPhone: " + cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    else
                        message += "\nPhone number not exist.";
                    cursor.close();
                } catch (Exception e){
                    message += "\nNo Permission To Get Phone Number.";
                }
                dialog.setTitle("Info").setMessage(message);
                dialog.setPositiveButton("OK", null);
                dialog.create().show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                // 处理长按事件
                final CommentInfo comment = commentList.get(position);
                if (comment.getUserName().equals(user.getName())) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(CommentActivity.this);
                    dialog.setMessage("Delete or not?");
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            database.deleteComment(comment.getCommentId());
                            myListViewAdapter.removeItem(position);
                            Toast.makeText(CommentActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.setNegativeButton("NO", null);
                    dialog.create().show();
                } else {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(CommentActivity.this);
                    dialog.setMessage("Report or not?");
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CommentActivity.this, "already reported.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.setNegativeButton("NO", null);
                    dialog.create().show();
                }
                return true;
            }
        });
        myListViewAdapter.refreshList(commentList);
    }

    public void SendButtonOnClick(View target) {
        String content = ((EditText)findViewById(R.id.edit_text)).getText().toString();
        if (content.equals("")) {
            Toast.makeText(this, "Comment cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(new Date());
        CommentInfo comment = new CommentInfo();
        comment.setUserName(user.getName());
        comment.setContent(content);
        comment.setDate(date);
        comment.setLikeSum(0);
        database.insertComment(comment);

        commentList = database.getCommentList(user.getName());
        myListViewAdapter.refreshList(commentList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "您已经拒绝读取通信录的权限", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
