package ai.autonumber.db

import java.sql.{ResultSet, SQLException}

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

  def getLastMessage(userId: String): ChatMessage = {
    println("Getting last message from " + userId)
    val query: String = "SELECT id, text,user_id, time  FROM xgb_autonumber.chatmessage where user_id = '" + userId + "' order by id desc LIMIT 1"
    val resultSet: ResultSet = DatabaseUtils.executeQuery(query)
    try {
      if (resultSet.next) {
        val chatMessage: ChatMessage = new ChatMessage
        chatMessage.setMessageId(resultSet.getString(1))
        chatMessage.setText(resultSet.getString(2))
        chatMessage.setUserId(resultSet.getString(3))
        chatMessage.setTime(resultSet.getString(4))
        return chatMessage
      }
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    }
    finally {
      try {
        resultSet.close()
      }
      catch {
        case e: SQLException =>
          e.printStackTrace()
      }
    }
    null
  }

  def getNewMessagesThen(lastMessageId: String): List[ChatMessage] = {
    val query: String = "SELECT id, text, user_Id, time FROM xgb_autonumber.chatMessage where id > '" + lastMessageId + "'"
    val resultSet: ResultSet = DatabaseUtils.executeQuery(query)
    var result: List[ChatMessage] = List.empty
    try {
      while (resultSet.next) {
        val chatMessage: ChatMessage = new ChatMessage
        chatMessage.setMessageId(resultSet.getString(1))
        chatMessage.setText(resultSet.getString(2))
        chatMessage.setUserId(resultSet.getString(3))
        chatMessage.setTime(resultSet.getString(4))
        result = result ++ List(chatMessage)
      }
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    }
    finally {
      try {
        resultSet.close()
      }
      catch {
        case e: SQLException =>
          e.printStackTrace()
      }
    }
    result
  }
}
