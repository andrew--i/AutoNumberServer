package ai.autonumber.db

import ai.autonumber.domain.domain.User

/**
 * Created by Andrew on 30.08.2014.
 */
object GameResultDao {
  def addNewResult(regId: String, image: String) = {
    println("Adding new game result from " + regId)
    val user: User = UserDao.findUserByRegId(regId)
    val searchCarNumber: Int = getSearchCarNumber()
    if (user != null) {
      val query: String = "INSERT INTO xgb_autonumber.gameresult (result,user_id,car_number) VALUES('" + image + "','" + user.id + "','" + searchCarNumber + "')"
      DatabaseUtils.execute(query)
    }
  }


  def getSearchCarNumber(): Int = {
    println("Getting search car number ")
    val query: String = "SELECT car_number  FROM xgb_autonumber.gameresult order by car_number desc LIMIT 1"
    DatabaseUtils.executeQuery(query, 0, rs => if (rs.next()) rs.getString(1).toInt else 0)
  }
}
