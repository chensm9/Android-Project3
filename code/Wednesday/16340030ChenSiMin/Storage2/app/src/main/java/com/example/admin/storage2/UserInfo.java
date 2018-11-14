package com.example.admin.storage2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class UserInfo {
    private int id;
    private String name;
    private String password;
    private Bitmap image;

    public int getId() { return id; }
    public void setId(int _id) { id = _id; }

    public String getName() { return name; }
    public void setName(String _name) { name = _name; }

    public String getPassword() { return password; }
    public void setPassword(String p) { password = p;}

    public Bitmap getImage() { return image; }
    public void setImage(Bitmap i) { image = i; }
    public void setImageByByte(byte[] data) {
        image = BitmapFactory.decodeByteArray(data, 0, data.length);
    }
    public byte[] getImageByte() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
