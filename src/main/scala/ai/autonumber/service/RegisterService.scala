package ai.autonumber.service

import ai.autonumber.db.UserDao
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}


class RegisterService extends AutoNumberService {

  override protected def isUriMatches(uri: String): Boolean = uri.equalsIgnoreCase("/register") || uri.equalsIgnoreCase("/unregister")

  override def processRequest(request: HttpRequest): Future[HttpResponse] = {
    val regId: String = paramValue(request, "regId")
    if (request.getUri.contains("/unregister")) {
      UserDao.unregister(regId);
    } else {
      val userName: String = paramValue(request, "user")
      UserDao.register(regId, userName)
    }
    successResponse()
  }
}
