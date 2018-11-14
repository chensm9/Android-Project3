package com.example.admin.storage2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<CommentInfo> list;
    private UserInfo user;
    private Context context;

    public MyAdapter(Context _context, List<CommentInfo> _list) {
        this.list = _list;
        this.context = _context;
    }
    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        }
        return list.get(i);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        // 新声明一个View变量和ViewHoleder变量,ViewHolder类在下面定义。
        View convertView ;
        ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            view = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.userName = view.findViewById(R.id.username);
            viewHolder.date = view.findViewById(R.id.date);
            viewHolder.content = view.findViewById(R.id.content);
            viewHolder.likeSum = view.findViewById(R.id.like_sum);
            viewHolder.likeImage = view.findViewById(R.id.like_image);
            viewHolder.userImage = view.findViewById(R.id.user_image);
            view.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
            convertView = view;
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final  CommentInfo comment = list.get(i);
        // 从viewHolder中取出对应的对象，然后赋值给他们
        viewHolder.userName.setText(comment.getUserName());
        viewHolder.date.setText(comment.getDate());
        viewHolder.likeSum.setText(""+comment.getLikeSum());
        viewHolder.content.setText(comment.getContent());
        viewHolder.userImage.setImageBitmap(comment.getUserImage());
        if (comment.getIsLike())
            viewHolder.likeImage.setImageResource(R.mipmap.red);
        else
            viewHolder.likeImage.setImageResource(R.mipmap.white);

        viewHolder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView im = (ImageView) v;
                comment.setIsLike(!comment.getIsLike());
                if (comment.getIsLike()) {
                    im.setImageResource(R.mipmap.red);
                    comment.setLikeSum(comment.getLikeSum()+1);
                    myDB.getInstance(null).insertLike(user.getName(), comment.getCommentId());
                    notifyDataSetChanged();
                }
                else {
                    im.setImageResource(R.mipmap.white);
                    comment.setLikeSum(comment.getLikeSum()-1);
                    myDB.getInstance(null).deleteLike(user.getName(), comment.getCommentId());
                    notifyDataSetChanged();
                }
            }
        });

        // 将这个处理好的view返回
        return convertView;
    }

    public void addItem(CommentInfo comment) {
        list.add(comment);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position < list.size()) {
            list.remove(position);
            notifyDataSetChanged();
        }
    }

    public void refreshList(List<CommentInfo> _list) {
        list = _list;
        notifyDataSetChanged();
    }

    public void setUser(UserInfo user_) {
        user = user_;
    }

    private class ViewHolder {
        public TextView userName;
        public TextView date;
        public TextView content;
        public TextView likeSum;
        public ImageView userImage;
        public ImageView likeImage;
    }
}