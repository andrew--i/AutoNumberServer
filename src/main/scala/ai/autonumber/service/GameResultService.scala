package ai.autonumber.service

import ai.autonumber.db.{GameResultDao, UserDao}
import ai.autonumber.gcm.CommunicationHelper
import ai.autonumber.gcm.model.Message
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}

import scala.collection.JavaConverters._

/**
 * Created by Andrew on 30.08.2014.
 */
class GameResultService extends AutoNumberService {
  override protected def isUriMatches(uri: String): Boolean = uri.equalsIgnoreCase("/newimage") || uri.equalsIgnoreCase("/searchNumber")

  override def processRequest(request: HttpRequest): Future[HttpResponse] = {
    if (path(request).contains("newimage")) {
      val regId: String = paramValue(request, "regId")
      val image: String = paramValue(request, "image")
      GameResultDao.addNewResult(regId, image)
      val message: Message = new Message.Builder().addData("new-search-car-number", GameResultDao.getSearchCarNumber().toString).build
      CommunicationHelper.getInstance.sendMessageTo(UserDao.getUsers.map(u => u.regId).asJava, message)
    } else if (path(request).contains("searchNumber")) {
      val regId: String = paramValue(request, "regId")
      val message: Message = new Message.Builder().addData("search-car-number", GameResultDao.getSearchCarNumber().toString).build
      CommunicationHelper.getInstance.sendMessageTo(regId, message)
    }

    successResponse()
  }
}
