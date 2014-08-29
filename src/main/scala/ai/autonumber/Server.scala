package ai.autonumber

import ai.autonumber.service.HomeService
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

  val handlers = List(new HomeService())

  def apply(request: HttpRequest): Future[HttpResponse] = {
    val uri: String = request.getUri
    for (service <- handlers) {
      if (uri.endsWith(service.requestUrl))
        return service.processRequest(request)
    }
    resourceNotFoundResponsse(request)
  }

  def resourceNotFoundResponsse(request: HttpRequest): Future[HttpResponse] = {
    val response = Response()
    response.setStatusCode(404)
    response.setContentString("Resource does not exist")
    Future(response)
  }
}
