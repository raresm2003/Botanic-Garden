package Notification;

public class Notification implements Notifier{
    @Override
    public void send(String message, String email, String phone) {
        Notifier en = new EmailNotifier(this);
        Notifier sn = new SmsNotifier(this);
        en.send(message, email, phone);
        sn.send(message, email, phone);
    }
}
