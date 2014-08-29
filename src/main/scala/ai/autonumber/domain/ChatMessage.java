package ai.autonumber.domain;

import org.json.simple.JSONValue;

import java.io.Serializable;
import java.util.HashMap;


/**
 * Created by Andrew on 27.08.2014.
 */
public class ChatMessage implements Serializable {
  private String userId;
  private String text;
  private String messageId;
  private String time;
  private String userName;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getTime() {
    return time;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String toJson() {
    final HashMap<String, String> value = new HashMap<String, String>();
    value.put("userId", userId);
    value.put("text", text);
    value.put("messageId", messageId);
    value.put("time", time);
    value.put("userName", userName);
    return JSONValue.toJSONString(value);
  }
}
