

packageArchetype.java_application

name := """auto-number"""

version := "1.0"

scalaVersion := "2.10.4"
  
libraryDependencies ++= Seq(
  "com.twitter" % "finagle-http_2.10" % "6.18.0",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.apache.commons" % "commons-dbcp2" % "2.0.1",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1"
)
