name := "graphql-europe"
description := "GraphQL-Europe Conference Website"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  ws,
  filters,
  guice,
  "org.sangria-graphql" %% "sangria" % "1.3.0",
  "org.sangria-graphql" %% "sangria-play-json" % "1.0.2",
  "com.vladsch.flexmark" % "flexmark-all" % "0.15.3",
  "com.iheart" %% "ficus" % "1.4.0",
  "org.apache.commons" % "commons-email" % "1.4"
)

routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).enablePlugins(PlayScala)

herokuAppName in Compile := "graphql-europe"