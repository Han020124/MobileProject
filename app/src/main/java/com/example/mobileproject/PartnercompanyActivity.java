package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class PartnercompanyActivity extends AppCompatActivity {

    private ImageButton btnArrow;
    private ListView lvPartnership;
    private List<Company> companyList;
    private CompanyAdapter companyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partnercompany);

        initViews();
        setupData();
        setupListView();
        setupListeners();
    }

    private void initViews() {
        btnArrow = findViewById(R.id.btn_arrow);
        lvPartnership = findViewById(R.id.lv_partnership);
    }

    private void setupData() {
        companyList = new ArrayList<>();

        companyList.add(new Company("ㄱㄱ안과", R.drawable.ic_company));
        companyList.add(new Company("ㄴㄴ피부과", R.drawable.ic_company));
        companyList.add(new Company("ㄷㄷ카페", R.drawable.ic_company));
        companyList.add(new Company("ㄹㄹ헤어", R.drawable.ic_company));
        companyList.add(new Company("ㅁㅁ카페", R.drawable.ic_company));
        companyList.add(new Company("ㅂㅂ식당", R.drawable.ic_company));
        companyList.add(new Company("ㅅㅅ의원", R.drawable.ic_company));
        companyList.add(new Company("ㅇㅇ병원", R.drawable.ic_company));
    }

    private void setupListView() {
        companyAdapter = new CompanyAdapter(this, companyList);
        lvPartnership.setAdapter(companyAdapter);
    }

    private void setupListeners() {

        btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartnercompanyActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lvPartnership.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Company selectedCompany = companyList.get(position);

                Intent intent = new Intent(PartnercompanyActivity.this, CompanyInfoActivity.class);

                intent.putExtra("company_name", selectedCompany.getName());
                intent.putExtra("company_logo", selectedCompany.getLogoResId());
                startActivity(intent);
            }
        });
    }

    public static class Company {
        private String name;
        private int logoResId;

        public Company(String name, int logoResId) {
            this.name = name;
            this.logoResId = logoResId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLogoResId() {
            return logoResId;
        }

        public void setLogoResId(int logoResId) {
            this.logoResId = logoResId;
        }
    }
}