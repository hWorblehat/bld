ThisBuild / scalaVersion := "2.12.7"
ThisBuild / organization  := "hWorblehat"

lazy val bld = (project in file("."))
	.settings(
		name := "bld",
		libraryDependencies += "com.chuusai"%%"shapeless"%"2.3.3",

		libraryDependencies += "org.scalatest"%%"scalatest"%"3.0.5"%Test,
		libraryDependencies += "junit"%"junit"%"4.12"%Test,
	)