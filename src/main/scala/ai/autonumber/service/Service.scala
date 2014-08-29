package ai.autonumber.service

import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpRequest}

/**
 * Created by Andrew on 29.08.2014.
 */
trait Service {
  val requestUrl:String
  def processRequest(request:HttpRequest):Future[HttpResponse]
}
