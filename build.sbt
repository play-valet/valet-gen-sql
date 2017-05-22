
lazy val root = (project in file("."))
  .settings(
    name := "valet-gen-sql",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1",
      "commons-io" % "commons-io" % "2.5",
      "org.skinny-framework" %% "skinny-framework" % "2.3.5",
      "org.skinny-framework" %% "skinny-task" % "2.3.5",
      "joda-time" % "joda-time" % "2.9.7",
      "org.joda" % "joda-convert" % "1.8.1",
      "org.scalikejdbc" % "scalikejdbc-core_2.11" % "2.5.1"
    )
  )
