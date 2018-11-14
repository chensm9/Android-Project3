package com.example.admin.storage2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class myDB extends SQLiteOpenHelper {
    private static final String USER_TABLE_NAME = "user";
    private static final String COMMENT_TABLE_NAME = "comment";
    private static final String LIKE_TABLE_NAME = "like_list";
    private static final int DB_VERSION = 1;

    // 单例模式
    private static myDB instance = null;
    public static myDB getInstance(Context context) {
        if(instance == null)
            instance = new myDB(context);
        return instance;
    }

    // 构造函数设置为私有
    private myDB(Context context) {
        super(context, "database.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE if not exists "
                + USER_TABLE_NAME + " (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "password TEXT, " +
                "image BLOB )";
        String CREATE_COMMENT_TABLE = "CREATE TABLE if not exists "
                + COMMENT_TABLE_NAME + " (" +
                "comment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "content TEXT, " +
                "date TEXT, " +
                "like_sum INTEGER )";
        String CREARE_LIKE_TABLE = "CREATE TABLE if not exists "
                + LIKE_TABLE_NAME + " (" +
                "like_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "comment_id INTEGER )";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_COMMENT_TABLE);
        sqLiteDatabase.execSQL(CREARE_LIKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {

    }

    // 根据名字获取用户信息
    public UserInfo getUserByName(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE name = '%s'",USER_TABLE_NAME, name );
        Cursor cursor = db.rawQuery( sql, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        else {
            UserInfo data = new UserInfo();
            data.setId(cursor.getInt(cursor.getColumnIndex("user_id")));
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            data.setImageByByte(cursor.getBlob(cursor.getColumnIndex("image")));
            cursor.close();
            return data;
        }
    }

    // 判断该名字用户是否存在
    public Boolean queryUserByName(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE name = '%s'",USER_TABLE_NAME, name );
        Cursor cursor = db.rawQuery( sql, null);
        Boolean if_exist = cursor.moveToFirst();
        cursor.close();
        return if_exist;
    }

    // 插入用户
    public void insertUser(UserInfo data) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", data.getName());
        cv.put("password", data.getPassword());
        cv.put("image", data.getImageByte());
        db.insert(USER_TABLE_NAME, null, cv);
        db.close();
    }

    // 插入评论
    public void insertComment(CommentInfo comment) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", comment.getUserName());
        cv.put("content", comment.getContent());
        cv.put("date", comment.getDate());
        cv.put("like_sum", comment.getLikeSum());
        db.insert(COMMENT_TABLE_NAME, null, cv);
        db.close();
    }

    // 删除评论
    public void deleteComment(int comment_id) {
        SQLiteDatabase db = getWritableDatabase();
        String DELETE_COMMENT = "DELETE FROM " +
                COMMENT_TABLE_NAME +
                " WHERE comment_id = " + comment_id;
        db.execSQL(DELETE_COMMENT);
    }

    // 获取所有的评论列表，参数用户名用于确定哪些评论被该用户点赞了
    public List<CommentInfo> getCommentList(String username) {
        SQLiteDatabase db = getWritableDatabase();
        List<CommentInfo> list = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s", COMMENT_TABLE_NAME);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            CommentInfo comment = new CommentInfo();
            comment.setCommentId(cursor.getInt(cursor.getColumnIndex("comment_id")));
            comment.setUserName(cursor.getString(cursor.getColumnIndex("username")));
            comment.setDate(cursor.getString(cursor.getColumnIndex("date")));
            comment.setLikeSum(cursor.getInt(cursor.getColumnIndex("like_sum")));
            comment.setContent(cursor.getString(cursor.getColumnIndex("content")));
            comment.setUserImage(getUserImageByName(comment.getUserName()));
            String sql2 = String.format(
                    Locale.CHINA, "SELECT * FROM %s WHERE username = '%s' and comment_id = %d",
                    LIKE_TABLE_NAME, username, comment.getCommentId());
            Cursor cursor2 = db.rawQuery(sql2, null);
            comment.setIsLike(cursor2.moveToFirst());
            cursor2.close();
            list.add(comment);
        }
        cursor.close();
        return list;
    }

    // 根据用户名获取该用户的图片
    public Bitmap getUserImageByName(String username) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = String.format("SELECT * FROM %s WHERE name = '%s'",USER_TABLE_NAME, username );
        Cursor cursor = db.rawQuery( sql, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        else {
            byte[] data = cursor.getBlob(cursor.getColumnIndex("image"));
            Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
            cursor.close();
            return image;
        }
    }

    // 插入点赞
    public void insertLike(String username, int comment_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("comment_id", comment_id);
        db.insert(LIKE_TABLE_NAME, null, cv);
        String UPDATE_COMMENT = String.format(
                Locale.CHINA,"UPDATE %s SET like_sum = like_sum + 1 WHERE comment_id = %d ",
                COMMENT_TABLE_NAME, comment_id
        );
        db.execSQL(UPDATE_COMMENT);
        db.close();
    }

    // 删除点赞
    public void deleteLike(String username, int comment_id) {
        SQLiteDatabase db = getWritableDatabase();
        String  DELETE_LIKE = String.format(
                Locale.CHINA,"DELETE FROM %s WHERE username = '%s' and comment_id = %d",
                LIKE_TABLE_NAME , username, comment_id
        );
        String UPDATE_COMMENT = String.format(
                Locale.CHINA,"UPDATE %s SET like_sum = like_sum - 1 where comment_id = %d ",
                COMMENT_TABLE_NAME, comment_id
        );
        db.execSQL(DELETE_LIKE);
        db.execSQL(UPDATE_COMMENT);
        db.close();
    }
}