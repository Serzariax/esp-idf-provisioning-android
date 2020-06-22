// Copyright 2020 Espressif Systems (Shanghai) PTE LTD
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.espressif.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.espressif.AppConstants;
import com.espressif.wifi_provisioning.R;
import com.espressif.provisioning.ESPProvisionManager;

import java.util.ArrayList;

public class ProofOfPossessionActivity extends AppCompatActivity {

    private static final String TAG = ProofOfPossessionActivity.class.getSimpleName();

    private TextView tvTitle, tvBack, tvCancel;
    private CardView btnNext;
    private TextView txtNextBtn;

    private String deviceName;
    private TextView tvPopInstruction;
    private EditText etPop;
    private ESPProvisionManager provisionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        provisionManager = ESPProvisionManager.getInstance(getApplicationContext());
        initViews();

        deviceName = provisionManager.getEspDevice().getDeviceName();

        if (!TextUtils.isEmpty(deviceName)) {
            String popText = getString(R.string.pop_instruction) + " " + deviceName;
            tvPopInstruction.setText(popText);
        }

        String pop = getResources().getString(R.string.proof_of_possesion);

        if (!TextUtils.isEmpty(pop)) {

            etPop.setText(pop);
            etPop.setSelection(etPop.getText().length());
        }
        etPop.requestFocus();
    }

    @Override
    public void onBackPressed() {
        provisionManager.getEspDevice().disconnectDevice();
        super.onBackPressed();
    }

    private View.OnClickListener nextBtnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            final String pop = etPop.getText().toString();
            Log.d(TAG, "POP : " + pop);
            provisionManager.getEspDevice().setProofOfPossession(pop);
            ArrayList<String> deviceCaps = provisionManager.getEspDevice().getDeviceCapabilities();

            if (deviceCaps.contains("wifi_scan")) {
                goToWiFiScanListActivity();
            } else {
                goToWiFiConfigActivity();
            }
        }
    };

    private View.OnClickListener cancelBtnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            finish();
        }
    };

    private void initViews() {

        tvTitle = findViewById(R.id.main_toolbar_title);
        tvBack = findViewById(R.id.btn_back);
        tvCancel = findViewById(R.id.btn_cancel);
        tvPopInstruction = findViewById(R.id.tv_pop);
        etPop = findViewById(R.id.et_pop);

        tvTitle.setText(R.string.title_activity_pop);
        tvBack.setVisibility(View.GONE);
        tvCancel.setVisibility(View.VISIBLE);

        tvCancel.setOnClickListener(cancelBtnClickListener);

        btnNext = findViewById(R.id.btn_next);
        txtNextBtn = findViewById(R.id.text_btn);

        txtNextBtn.setText(R.string.btn_next);
        btnNext.setOnClickListener(nextBtnClickListener);
    }

    private void goToWiFiScanListActivity() {

        Intent wifiListIntent = new Intent(getApplicationContext(), WiFiScanActivity.class);
        wifiListIntent.putExtras(getIntent());
        startActivity(wifiListIntent);
        finish();
    }

    private void goToWiFiConfigActivity() {

        Intent wifiConfigIntent = new Intent(getApplicationContext(), WiFiConfigActivity.class);
        wifiConfigIntent.putExtras(getIntent());
        startActivity(wifiConfigIntent);
        finish();
    }
}
