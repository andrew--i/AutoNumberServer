package ai.autonumber.domain

import org.json.simple.JSONValue

/**
 * Created by Andrew on 30.08.2014.
 */
object domain {

  trait ToJson {
    def toJson: String
  }

  case class GameResult(id: String, userId: String, result: String, time: String, carNumber: Int) extends ToJson {
    override def toJson: String = {
      JSONValue.toJSONString(Map(
        "userId" -> userId,
        "id" -> id,
        "time" -> time,
        "result" -> result,
        "carNumber" -> carNumber
      ))
    }
  }

  case class ChatMessage(userId: String, text: String, messageId: String, time: String, userName: String) extends ToJson {
    override def toJson: String = {
      JSONValue.toJSONString(Map(
        "userId" -> userId,
        "messageId" -> messageId,
        "time" -> time,
        "userName" -> userName,
        "text" -> text
      ))
    }
  }

  object ChatMessage {
    def apply(message: ChatMessage, userName: String): ChatMessage = {
      new ChatMessage(message.userId, message.text, message.messageId, message.time, userName)
    }
  }

  case class User(id: String, regId: String, name: String, readableName: String) extends ToJson {
    override def toJson: String = {
      JSONValue.toJSONString(Map(
        "id" -> id,
        "name" -> name,
        "regId" -> regId,
        "readableName" -> readableName
      ))
    }
  }

}
