package com.example.qthjen.webservicedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomAdapter extends BaseAdapter{

    MainActivity mContext;
    int mLayout;
    List<SinhVien> mList;

    public CustomAdapter(MainActivity mContext, int mLayout, List<SinhVien> mList) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {

        TextView tvHoTen;
        TextView tvNamSinh;
        TextView tvDiaChi;
        ImageView ivDelete, ivEdit;

    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();
        if ( view == null) {

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(mLayout, null);
            viewHolder.tvHoTen = (TextView) view.findViewById(R.id.tvHoTen);
            viewHolder.tvNamSinh = (TextView) view.findViewById(R.id.tvNamSinh);
            viewHolder.tvDiaChi = (TextView) view.findViewById(R.id.tvDiaChi);
            viewHolder.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
            viewHolder.ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvHoTen.setText(mList.get(i).getHoTen());
        viewHolder.tvNamSinh.setText(mList.get(i).getNamSinh());
        viewHolder.tvDiaChi.setText(mList.get(i).getDiaChi());

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.EditDialog(mList.get(i).getId(), mList.get(i).getHoTen(), mList.get(i).getNamSinh(), mList.get(i).getDiaChi());
            }
        });

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.DeleteDialog(mList.get(i).getId());
            }
        });

        return view;
    }
}
