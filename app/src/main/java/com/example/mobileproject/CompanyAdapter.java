package com.example.mobileproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CompanyAdapter extends BaseAdapter {

    private Context context;
    private List<com.example.mobileproject.PartnercompanyActivity.Company> companyList;
    private LayoutInflater inflater;

    public CompanyAdapter(Context context, List<com.example.mobileproject.PartnercompanyActivity.Company> companyList) {
        this.context = context;
        this.companyList = companyList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return companyList != null ? companyList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return companyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_partnercompany, parent, false);
            holder = new ViewHolder();
            holder.ivCompanyLogo = convertView.findViewById(R.id.iv_company_logo);
            holder.tvCompanyName = convertView.findViewById(R.id.tv_company_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        com.example.mobileproject.PartnercompanyActivity.Company company = companyList.get(position);

        holder.ivCompanyLogo.setImageResource(company.getLogoResId());

        holder.tvCompanyName.setText(company.getName());

        return convertView;
    }

    static class ViewHolder {
        ImageView ivCompanyLogo;
        TextView tvCompanyName;
    }
}