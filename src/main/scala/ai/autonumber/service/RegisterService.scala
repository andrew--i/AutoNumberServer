package ai.autonumber.service

import ai.autonumber.db.UserDao
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}

/**
 * Created by Andrew on 29.08.2014.
 */
class RegisterService extends AutoNumberService {

  override protected def isUriMatches(uri: String): Boolean = uri.equalsIgnoreCase("/register") || uri.equalsIgnoreCase("/unregister")

  override def processRequest(request: HttpRequest): Future[HttpResponse] = {
    val regId: String = paramValue(request, "regId")
    if (request.getUri.contains("/unregister")) {
      UserDao.unregister(regId);
    } else {
      val userName: String = paramValue(request, "userName")
      UserDao.register(regId, userName)
    }
    successResponse()
  }
}
