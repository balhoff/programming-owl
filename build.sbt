
organization  := "org.renci"

name          := "programming-owl"

version       := "0.1"

scalaVersion  := "2.12.3"

libraryDependencies ++= {
  Seq(
    "net.sourceforge.owlapi"      %  "owlapi-distribution"    % "4.3.1",
    "org.phenoscape"              %% "scowl"                  % "1.3"
  )
}
