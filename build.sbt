enablePlugins(ScalaJSPlugin)

name := "scalajs-react-semantic-component"

jsDependencies += RuntimeDOM

libraryDependencies ++= Seq(
  "com.github.japgolly.scalajs-react" %%% "core" % "0.11.1",
  "org.scalaz" %%% "scalaz-core" % "7.2.4"
)

lazy val commonSettings = Seq(
  organization := "ssr.bayview",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.8"
)

workbenchSettings

lazy val root = project.in(file("."))
  .aggregate(core, examples)
  .settings(commonSettings: _*)

lazy val core = project.in(file("core"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % "0.11.1",
      "org.scalaz" %%% "scalaz-core" % "7.2.4"
    )).enablePlugins(ScalaJSPlugin)

lazy val examples = project.in(file("examples"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.0"
    ),
    jsDependencies ++= Seq(
      "org.webjars.bower" % "react" % "15.1.0"
        / "react-with-addons.js"
        minified "react-with-addons.min.js"
        commonJSName "React",

      "org.webjars.bower" % "react" % "15.1.0"
        / "react-dom.js"
        minified "react-dom.min.js"
        dependsOn "react-with-addons.js"
        commonJSName "ReactDOM",

      "org.webjars.bower" % "react" % "15.1.0"
        / "react-dom-server.js"
        minified "react-dom-server.min.js"
        dependsOn "react-dom.js"
        commonJSName "ReactDOMServer"),
    persistLauncher in Compile := true,
    skip in packageJSDependencies := false
  ).dependsOn(core).enablePlugins(ScalaJSPlugin)



bootSnippet := "scalajs.semanticui.ExampleApp().main();"

localUrl :=("127.0.0.1", 12345)

refreshBrowsers <<= refreshBrowsers.triggeredBy(fullOptJS in Compile)
