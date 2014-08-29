package ai.autonumber.db

import java.sql.ResultSet

import ai.autonumber.domain.User

/**
 * Created by Andrew on 29.08.2014.
 */
object UserDao {
  /**
   * Registers a device.
   */
  def register(regId: String, userName: String) {
    println("Registering " + regId)
    val query: String = "INSERT INTO xgb_autonumber.user (name, reg_id) VALUES('" + userName + "','" + regId + "')"
    DatabaseUtils.execute(query)
  }

  /**
   * Unregisters a device.
   */
  def unregister(regId: String) {
    println("Unregistering " + regId)
    val query: String = "DELETE FROM xgb_autonumber.user where reg_id = '" + regId + "'"
    DatabaseUtils.execute(query)
  }

  /**
   * Updates the registration id of a device.
   */
  def updateRegistration(oldId: String, newId: String) {
    println("Updating " + oldId + " to " + newId)
    val query: String = "UPDATE xgb_autonumber.user set  reg_id = '" + newId + "' where reg_id = '" + oldId + "'"
    DatabaseUtils.execute(query)
  }

  def userMarchaller(resultSet: ResultSet): List[User] = {
    var result: List[User] = List.empty
    while (resultSet.next) {
      val user: User = new User
      user.setId(resultSet.getString(1))
      user.setName(resultSet.getString(2))
      user.setRegId(resultSet.getString(3))
      result = result ++ List(user)
    }
    result
  }

  /**
   * Gets all registered devices.
   */
  def getUsers: List[User] = {
    val query: String = "SELECT id, name, reg_Id FROM xgb_autonumber.user"
    DatabaseUtils.executeQuery(query, List.empty, userMarchaller)
  }

  def findUserByRegId(regId: String): User = {
    val query: String = "SELECT id, name, reg_Id FROM xgb_autonumber.user where reg_id ='" + regId + "'"
    val users: List[User] = DatabaseUtils.executeQuery(query, List.empty, userMarchaller)
    if (users.isEmpty) null else users(0)
  }
}
