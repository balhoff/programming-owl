
organization  := "org.renci"

name          := "programming-owl"

version       := "0.1"

licenses := Seq("BSD-3-Clause" -> url("https://opensource.org/licenses/BSD-3-Clause")) //CC-ZERO

scalaVersion  := "2.12.3"

libraryDependencies ++= {
  Seq(
    "net.sourceforge.owlapi"      %  "owlapi-distribution"    % "4.3.1",
    "org.phenoscape"              %% "scowl"                  % "1.3",
    "org.semanticweb.elk"         %  "elk-owlapi"             % "0.4.3"
  )
}
