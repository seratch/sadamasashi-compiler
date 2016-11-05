lazy val root = (project in file("."))
  .settings(
    organization := "com.github.seratch",
    name := "sadamasashi-compiler",
    version := "0.2",
    scalaVersion := "2.12.0",
    crossScalaVersions := Seq("2.12.0", "2.11.8"),
    initialCommands := "import sadamasashi._",
    dependencyOverrides += "org.slf4j" % "slf4j-api" % "1.7.21",
    libraryDependencies ++= Seq(
      "org.scala-lang"       %  "scala-compiler"              % scalaVersion.value,
      "org.scala-lang"       %  "scala-reflect"               % scalaVersion.value,
      "org.skinny-framework" %% "skinny-common"               % "2.3.0-RC1",
      "org.apache.lucene"    %  "lucene-analyzers-kuromoji"   % "6.2.1",
      "org.apache.lucene"    %  "lucene-suggest"              % "6.2.1",
      "org.slf4j"            %  "slf4j-simple"                % "1.7.21",
      "com.google.apis"      %  "google-api-services-youtube" % "v3-rev179-1.22.0",
      "org.scalatest"        %% "scalatest"                   % "3.0.0" % Test
    ),
    transitiveClassifiers in Global := Seq(Artifact.SourceClassifier),
    incOptions := incOptions.value.withNameHashing(true),
    scalacOptions ++= Seq("-Xlint", "-unchecked", "-deprecation", "-feature"),
    publishMavenStyle := true,
    pomIncludeRepository := { x => false },
    pomExtra := <url>https://github.com/seratch/sadamasashi-compiler/</url>
  <licenses>
    <license>
      <name>MIT License</name>
      <url>http://www.opensource.org/licenses/mit-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:seratch/sadamasashi-compiler.git</url>
    <connection>scm:git:git@github.com:seratch/sadamasashi-compiler.git</connection>
  </scm>
  <developers>
    <developer>
      <id>seratch</id>
      <name>Kazuhiro Sera</name>
      <url>http://git.io/sera</url>
    </developer>
  </developers>
  )
  .settings(scalariformSettings)
