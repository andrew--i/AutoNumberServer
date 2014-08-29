package ai.autonumber.service

import ai.autonumber.db.{MessageDao, UserDao}
import ai.autonumber.domain.{ChatMessage, User}
import ai.autonumber.gcm.CommunicationHelper
import ai.autonumber.gcm.model.Message
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}
import org.postgresql.util.Base64

import scala.collection.JavaConverters._

class ChatService extends AutoNumberService {
  override protected def isUriMatches(uri: String): Boolean =
    uri.equalsIgnoreCase("/chat") || uri.equalsIgnoreCase("/messages")

  override def processRequest(request: HttpRequest): Future[HttpResponse] = {
    val regId: String = paramValue(request, "regId")
    if (path(request).equalsIgnoreCase("/chat")) {
      val msg: String = paramValue(request, "msg")
      val user: User = UserDao.findUserByRegId(regId)
      if (user != null) {
        MessageDao.addUserMessage(user.getId, msg)
        val lastMessage: ChatMessage = MessageDao.getLastMessage(user.getId)

        val users: List[User] = UserDao.getUsers

        users.find(u => lastMessage.getUserId.equalsIgnoreCase(u.getId)) match {
          case Some(u) => lastMessage.setUserName(u.getName)
          case None =>
        }
        val value: String = lastMessage.toJson
        val encodeBytes: String = Base64.encodeBytes(value.getBytes)
        val chatMessage: Message = new Message.Builder().addData("chat-message", encodeBytes).build
        val devices: List[String] = users.map(u => u.getRegId)
        CommunicationHelper.getInstance.sendMessageTo(devices.asJava, chatMessage)
      }
    } else {
      val lastMessageId: String = if(params(request).contains("lastMsgId")) paramValue(request, "lastMsgId") else "-1"
      val messages: List[ChatMessage] = MessageDao.getNewMessagesThen(lastMessageId)
      val users: List[User] = UserDao.getUsers
      for (chatMessage <- messages) {
        chatMessage.setUserName(getUserNameOfMessage(chatMessage, users))
        val value: String = chatMessage.toJson
        val message: Message = new Message.Builder().addData("chat-message", Base64.encodeBytes(value.getBytes)).build
        CommunicationHelper.getInstance.sendMessageTo(regId, message)
      }
    }
    successResponse()
  }

  private def getUserNameOfMessage(chatMessage: ChatMessage, users: List[User]): String = {
    for (user <- users) {
      if (chatMessage.getUserId.equalsIgnoreCase(user.getId))
        return user.getName
    }
    null
  }

}
