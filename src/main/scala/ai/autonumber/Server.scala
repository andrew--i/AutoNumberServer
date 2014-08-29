package ai.autonumber

import ai.autonumber.service.{HomeService, RegisterService}
import com.twitter.finagle.http.Response
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, Future}
import org.jboss.netty.handler.codec.http._

import scala.util.Properties

object Server {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    println("Starting on port: " + port)

    val server = Http.serve(":" + port, new AutoNumberService)
    Await.ready(server)
  }
}

class AutoNumberService extends Service[HttpRequest, HttpResponse] {

  val handlers = List(new HomeService(), new RegisterService())


  def apply(request: HttpRequest): Future[HttpResponse] = {

    try {
      for (service <- handlers) {
        if (service.isRequestMatches(request))
          return service.processRequest(request)
      }
    } catch {
      case e: Exception => return badRequestResponse(e)
    }
    resourceNotFoundResponse(request)
  }

  def resourceNotFoundResponse(request: HttpRequest): Future[HttpResponse] = {
    val response = Response()
    response.setStatusCode(404)
    response.setContentString("Resource does not exist")
    Future(response)
  }

  def badRequestResponse(value: Exception) = {
    val response = Response()
    response.setStatusCode(400)
    response.setContentString(value.getMessage)
    Future(response)
  }
}
