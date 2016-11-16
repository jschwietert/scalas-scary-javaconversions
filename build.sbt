name := "Scala's Scary JavaConversions"

version := "0.1"
scalaVersion := "2.11.8"

libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.6" % "test"

scalacOptions in Test ++= Seq("-Yrangepos")
