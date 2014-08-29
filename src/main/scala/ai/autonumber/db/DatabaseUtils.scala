package ai.autonumber.db

import java.sql._

import org.apache.commons.dbcp2.BasicDataSource

/**
 * Created by Andrew on 29.08.2014.
 */
object DatabaseUtils {
  private final val dbUrl: String = "jdbc:postgresql://postgres48.1gb.ru:5432/xgb_autonumber"
  private final val user: String = "xgb_autonumber"
  private final val password: String = "91d85239ps"


  lazy val dataSource: BasicDataSource = {
    val dataSource: BasicDataSource = new BasicDataSource()
    dataSource.setDriverClassName("org.postgresql.Driver")
    dataSource.setUrl(dbUrl)
    dataSource.setUsername(user)
    dataSource.setInitialSize(3)
    dataSource
  }

  def executeQuery[T](query: String, elseValue: T, marshaller: ResultSet => T): T = {
    var connection: Connection = null
    var statement: Statement = null
    var resultSet: ResultSet = null
    try {
      connection = dataSource.getConnection
      statement = connection.createStatement()
      resultSet = statement.executeQuery(query)
      return marshaller(resultSet)
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    } finally {
      connection.close()
      statement.close()
      resultSet.close()
    }
    elseValue
  }

  def execute[T](query: String): Boolean = {
    var connection: Connection = null
    var statement: Statement = null
    var resultSet: ResultSet = null
    try {
      connection = dataSource.getConnection
      statement = connection.createStatement()
      return statement.execute(query)
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    } finally {
      connection.close()
      statement.close()
    }
    false
  }
}


abstract class EntityMarshaller[T](query: String) {
  def marshall(r: ResultSet): T

  val elseValue: T
}

