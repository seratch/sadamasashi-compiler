scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
addMavenResolverPlugin
addSbtPlugin("com.typesafe.sbt"     % "sbt-scalariform" % "1.3.0")
addSbtPlugin("com.github.mpeltonen" % "sbt-idea"        % "1.6.0")
addSbtPlugin("org.xerial.sbt"       % "sbt-sonatype"    % "1.0")
addSbtPlugin("com.jsuereth"         % "sbt-pgp"         % "1.0.0")
