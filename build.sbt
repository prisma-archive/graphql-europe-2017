name := "graphql-europe"
description := "GraphQL-Europe Conference Website"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  ws,
  filters,
  "com.iheart" %% "ficus" % "1.4.0"
)

routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).enablePlugins(PlayScala)