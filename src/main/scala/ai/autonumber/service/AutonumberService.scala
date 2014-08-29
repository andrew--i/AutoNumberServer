package ai.autonumber.service

import com.twitter.finagle.http.Response
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}


abstract class AutoNumberService {

  def isRequestMatches(request: HttpRequest): Boolean = {
    isUriMatches(path(request))
  }

  protected def isUriMatches(uri: String): Boolean

  def processRequest(request: HttpRequest): Future[HttpResponse]

  def path(request: HttpRequest): String = {
    val uri: String = request.getUri
    if (uri.contains("?"))
      uri.substring(0, uri.indexOf("?"))
    uri
  }

  def params(request: HttpRequest): Map[String, String] = {
    val uri: String = request.getUri
    if (!uri.contains("?"))
      return Map.empty
    val params: String = uri.substring(uri.indexOf("?") + 1)
    params.split("&").map(s => s.split("=")).map(r => r(0) -> r(1)).foldLeft(Map.empty[String, String]) { (m, k) => m ++ Map(k)}
  }

  def paramValue(request: HttpRequest, paramName: String): String = {
    val parameters: Map[String, String] = params(request)
    if (parameters.contains(paramName))
      parameters(paramName)
    else
      throw new IllegalArgumentException("Param " + paramName + " not found")
  }

  protected def successResponse(): Future[HttpResponse] = {
    val response = Response()
    response.setStatusCode(200)
    Future(response)
  }
}
