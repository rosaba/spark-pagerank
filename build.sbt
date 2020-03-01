name := "Assignment3"

version := "0.1"

scalaVersion := "2.12.7"

lazy val sparkDependencies = Seq(
  //spark context, spark config ...
  "org.apache.spark" %% "spark-core" % "2.4.0",
  //dataframe ...
  "org.apache.spark" %% "spark-sql" % "2.4.0"
)

//testing dependencies
lazy val testDependencies = Seq(
  "junit" % "junit" % "4.12" % "test",
  "org.scalactic" %% "scalactic" % "3.0.5" % "test",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

libraryDependencies ++= testDependencies
libraryDependencies ++= sparkDependencies

/**
  * If you are getting this error: https://github.com/FasterXML/jackson-module-scala/issues/221 ,
  * uncomment the lines below and afterwards refresh the sbt dependencies.
  * To refresh sbt do the following:
  * For Intleij Idea View->Tool Window-> sbt -> refresh
  * For Scala IDE
  * - open up a terminal and execute "sbt eclipse"
  * - afterwards refresh the project - right click it and press "Refresh"
  */
/*
val jacksonVersion = "2.9.6"
dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
  "com.fasterxml.jackson.module" % "jackson-module-paranamer" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion
)
*/