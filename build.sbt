
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

// command : sbt "genSql/run hocon $(pwd)/valet.conf"
lazy val genSql = (project in file("valet/valet-gen-sql"))
  .settings(
    name := "valet-gen-sql",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8", "-Xlint:-options"),
    javacOptions in doc := Seq("-source", "1.8"),
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

// command : sbt "dbMigration/flywayMigrate"
lazy val dbMigration = (project in file("valet/valet-flyway"))
  .settings(
    name         := "valet-flyway",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "mysql" % "mysql-connector-java" % "5.1.40",
      "org.flywaydb" % "flyway-core" % "4.0",
      "org.flywaydb" % "flyway-sbt" % "4.1.2"
    ),
    flywayUrl := "jdbc:mysql://127.0.0.1/test?characterEncoding=UTF8&autoReconnect=true&useSSL=false",
    flywayUser := "test",
    flywayPassword := "test",
    flywayDriver := "com.mysql.jdbc.Driver",
    flywayLocations := Seq("filesystem:conf/db/migration/default")
  )
// command : sbt "genTables/compile"
lazy val genTables = (project in file("valet/valet-gen-slick-tables"))
  .settings(
    name         := "valet-gen-slick-tables",
    scalaVersion := "2.11.8",
    libraryDependencies ++= List(
      "com.typesafe.slick" %% "slick" % "3.1.1",
      "com.typesafe.slick" %% "slick-codegen" % "3.1.1",
      "org.slf4j" % "slf4j-nop" % "1.7.19",
      "mysql" % "mysql-connector-java" % "5.1.40"
    ),
    sourceGenerators in Compile <+= (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
      val outputDir = file(".").getCanonicalPath() + "/app"
      val url = "jdbc:mysql://127.0.0.1/test?characsbt ruterEncoding=UTF8&autoReconnect=true&useSSL=false"
      val jdbcDriver = "com.mysql.jdbc.Driver"
      val slickDriver = "slick.driver.MySQLDriver"
      val pkg = "models.entity"
      val user = "test"
      val password = "test"
      toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, user, password), s.log))
      val fname = outputDir + "/models/entity/Tables.scala"
      Seq(file(fname))
    }
  )
// command : sbt "genMVC/run mvc $(pwd)/valet.conf"
// -----------------------------
lazy val genMVC = (project in file("valet/valet-gen-mvc"))
  .settings(
    name := "valet-gen-mvc",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1",
      "commons-io" % "commons-io" % "2.5",
      "org.scalameta" %% "scalameta" % "1.6.0",
      "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0",
      "org.jsoup" % "jsoup" % "1.10.2",
      //      "net.ruippeixotog" %% "scala-scraper" % "1.2.0",
      "com.typesafe.play" %% "play-slick" % "2.0.2",
      "org.skinny-framework" %% "skinny-framework" % "2.3.5",
      "org.skinny-framework" %% "skinny-task" % "2.3.5",
      "joda-time" % "joda-time" % "2.9.7",
      "org.joda" % "joda-convert" % "1.8.1",
      "org.typelevel" %% "cats" % "0.9.0",
      "org.scalaz" %% "scalaz-core" % "7.2.11",
      "com.github.pathikrit" %% "better-files" % "2.17.1",
      "org.scalikejdbc" % "scalikejdbc-core_2.11" % "2.5.1"
    )
  )
