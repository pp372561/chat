package bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String sender;
    private String content;
    private String time;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String getSender() { return sender; }
    public String getContent() { return content; }
    public String getTime() { return time; }
}
