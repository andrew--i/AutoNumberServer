package ai.autonumber.db

import java.sql.ResultSet

import ai.autonumber.domain.ChatMessage

/**
 * Created by Andrew on 29.08.2014.
 */
object MessageDao {
  def addUserMessage(userId: String, message: String) {
    println("Registering chat message " + message + " from " + userId)
    val query: String = "INSERT INTO xgb_autonumber.chatmessage (text,user_id) VALUES('" + message + "','" + userId + "')"
    DatabaseUtils.execute(query)
  }


  def chatMessageMarshaller(resultSet: ResultSet): List[ChatMessage] = {
    var result: List[ChatMessage] = List.empty
    while (resultSet.next) {
      val chatMessage: ChatMessage = new ChatMessage
      chatMessage.setMessageId(resultSet.getString(1))
      chatMessage.setText(resultSet.getString(2))
      chatMessage.setUserId(resultSet.getString(3))
      chatMessage.setTime(resultSet.getString(4))
      result = result ++ List(chatMessage)
    }
    result
  }

  def getLastMessage(userId: String): ChatMessage = {
    println("Getting last message from " + userId)
    val query: String = "SELECT id, text,user_id, time  FROM xgb_autonumber.chatmessage where user_id = '" + userId + "' order by id desc LIMIT 1"
    val chatMessages: List[ChatMessage] = DatabaseUtils.executeQuery(query, List.empty, chatMessageMarshaller)
    if (chatMessages.isEmpty) null else chatMessages(0)
  }

  def getNewMessagesThen(lastMessageId: String): List[ChatMessage] = {
    val query: String = "SELECT id, text, user_Id, time FROM xgb_autonumber.chatMessage where id > '" + lastMessageId + "' LIMIT 10"
    DatabaseUtils.executeQuery(query, List.empty, chatMessageMarshaller)
  }
}
