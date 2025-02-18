package Notification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsNotifier implements Notifier {
    private Notifier wrappedSms;

    public SmsNotifier(Notifier wrappedSms) {
        this.wrappedSms = wrappedSms;
    }

    @Override
    public void send(String message, String email, String phone) {
        final String ACCOUNT_SID = "AC53870732bffb80639fccecd510316eb4";
        final String AUTH_TOKEN = "993d3ac294db9526f82044a1ec58f8c6";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message mess = Message.creator(
                new PhoneNumber("+4" + phone),
                new PhoneNumber("+14238965753"),
                message).create();

        System.out.println(mess.getSid());
    }
}