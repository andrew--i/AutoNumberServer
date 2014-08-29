package ai.autonumber.db

import java.sql._

/**
 * Created by Andrew on 29.08.2014.
 */
object DatabaseUtils {
  private final val dbUrl: String = "jdbc:postgresql://postgres48.1gb.ru:5432/xgb_autonumber"
  private final val user: String = "xgb_autonumber"
  private final val password: String = "91d85239ps"


  private def connection: Connection = {
    DriverManager.getConnection(dbUrl, user, password)
  }

  def executeQuery(query: String): ResultSet = {
    try {
      val statement: Statement = connection.createStatement()
      return statement.executeQuery(query)
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    }
    null
  }

  def execute(query: String): Boolean = {
    try {
      val statement: Statement = connection.createStatement()
      return statement.execute(query)
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    }
    false
  }
}

