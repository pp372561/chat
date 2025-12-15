package bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息实体类
 */
public class Message {
    private static long idCounter = 0;
    private final long id;
    private String sender;
    private String content;
    private String time;
    private MessageType type;
    private String receiver;

    // 普通群消息构造方法
    public Message(String sender, String content) {
        this.id = ++idCounter;
        this.sender = sender;
        this.content = content;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.type = MessageType.NORMAL;
    }

    // 私聊消息构造方法
    public Message(String sender, String content, String receiver) {
        this.id = ++idCounter;
        this.sender = sender;
        this.content = content;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.type = MessageType.PRIVATE;
        this.receiver = receiver;
    }

    // 系统通知构造方法
    public Message(String content) {
        this.id = ++idCounter;
        this.sender = "系统";
        this.content = content;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.type = MessageType.SYSTEM;
    }

    // Getter方法
    public long getId() { return id; }
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public String getTime() { return time; }
    public MessageType getType() { return type; }
    public String getReceiver() { return receiver; }
}