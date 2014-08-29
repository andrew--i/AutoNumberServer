package ai.autonumber.service

import ai.autonumber.db.UserDao
import ai.autonumber.domain.User
import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}


class HomeService extends AutoNumberService {

  override protected def isUriMatches(uri: String): Boolean = uri.equalsIgnoreCase("/")

  override def processRequest(request: HttpRequest): Future[HttpResponse] = {
    val response = Response()
    response.setStatusCode(200)
    response.setContentType(mediaType = "text/html", "utf-8")
    var content: String = "<html><body>" + "<head>" +
        "  <title>AutoNumber Demo</title>" + "</head>"
    val users: List[User] = UserDao.getUsers
    if (users.isEmpty) {
      content = content + "<h2>No users registered!</h2>"
    } else {
      content = content + "<h2>" + users.size + " user(s) registered!</h2>"
      content = content + "<ul>"
      for (user <- users) {
        content = content + "<li>"
        content = content + user.getName + "( " + user.getRegId + ")"
        content = content + "</li>"
      }
      content = content + "</ul>"
    }

    content = content + "</body></html>"

    response.setContentString(content)
    Future(response)
  }
}
