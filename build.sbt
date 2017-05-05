name := """UltimalHR"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

PlayKeys.externalizeResources := false

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  "org.hibernate.javax.persistence" % "hibernate-jpa-2.1-api" % "1.0.0.Final",
  "mysql" % "mysql-connector-java" % "5.1.16",
  "org.projectlombok" % "lombok" % "1.16.12",
  "javax.mail" % "javax.mail-api" % "1.5.4",
  "org.apache.commons" % "commons-lang3" % "3.5",
  "org.elasticsearch" % "elasticsearch" % "2.3.4",
  "com.google.code.gson" % "gson" % "2.3.1",
  "org.mongodb.morphia" % "morphia" % "1.0.0",
  "org.apache.poi" % "poi" % "3.12",
  "org.apache.poi" % "poi-ooxml" % "3.9",
  "com.sun.mail" % "javax.mail" % "1.5.4",
  "com.google.guava" % "guava" % "19.0",
  "com.googlecode.libphonenumber" % "libphonenumber" % "7.4.0"
)
