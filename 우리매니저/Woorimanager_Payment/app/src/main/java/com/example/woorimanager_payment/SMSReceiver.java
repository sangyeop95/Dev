package com.example.woorimanager_payment;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class SMSReceiver extends BroadcastReceiver {
    private final ServerManager serverManager = new ServerManager();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");
                /**
                 * PDU = Protocol Data Unit
                 * format = "3gpp" : 3rd Generation Partnership Project (제3세대 파트너십 프로젝트) => 이동 통신 시스템과 표준을 개발하는 국제 표준화 단체
                 * GSM(Global System for Mobile Communications)과 같은 2G, 3G, 4G, 5G 등의 통신 기술과 관련된 표준을 개발하고 유지보수합니다.
                 * SMS(Short Message Service)는 이동 통신에서 사용되는 메시지 서비스로, 짧은 텍스트 메시지를 주고받을 수 있게 해줍니다. 
                 * SMS는 이동 통신망에서 전달되는 데이터 형식을 갖고 있는데, 이를 SMS PDU(Protocol Data Unit)라고 합니다. 
                 * SMS PDU는 이동 통신망 간에 SMS 메시지를 전송하고 해석하는데 사용되는 형식입니다.
                 * "3gpp" 형식은 3GPP에서 정의한 SMS PDU 형식을 의미합니다.
                 * 3GPP는 2G, 3G, 4G, 5G 등 다양한 이동 통신 기술을 포함하는 표준을 관리하는 단체이므로, "3gpp" 형식은 3GPP에서 정의한 SMS PDU 형식을 사용하여 SMS 메시지를 해석하는 데 사용됩니다.
                 * 따라서, "3gpp" 형식은 이동 통신에서 사용되는 SMS 메시지의 데이터 형식 중 하나를 나타냅니다.
                 */
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
                        String address = smsMessage.getOriginatingAddress();
                        String body = smsMessage.getMessageBody();

                        StringBuilder messageBody = new StringBuilder();
                        messageBody.append(String.format("%s@@%s", address, body)).append("||");

                        String phoneNumber = getPhoneNumber(context);
                        try {
                            serverManager.messageToServer(messageBody.toString(), phoneNumber);
                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private String getPhoneNumber(Context context) {
        if (ActivityCompat.checkSelfPermission(context, READ_PHONE_STATE) == PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, READ_SMS) == PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            String phoneNumber = tm.getLine1Number().replace("+", "");
            return !phoneNumber.startsWith("82") && phoneNumber.startsWith("0") ? "82" + phoneNumber.substring(1) : phoneNumber;
        }
        return null;
    }
}