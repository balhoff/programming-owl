package org.renci.owl

import java.io.File
import java.util.UUID

import scala.collection.JavaConverters._
import scala.io.Source

import org.phenoscape.scowl._
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.IRI
import org.semanticweb.owlapi.model.OWLAxiom

/**
 * Read in a TSV file including labels and IRIs for term pairs for which you want to create named compositions.
 */
object ConvertTableToOWL extends App {

  val PartOf = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000050")

  def convertLine(line: String): Set[OWLAxiom] = {
    val items = line.split("\t", -1)
    val partLabel = items(0)
    val partTerm = Class(items(1))
    val wholeLabel = items(2)
    val wholeTerm = Class(items(3))
    val newTerm = Class(s"http://example.org/${UUID.randomUUID.toString}")
    var axioms = Set.empty[OWLAxiom]
    axioms += newTerm EquivalentTo (partTerm and (PartOf some wholeTerm))
    axioms += newTerm Annotation (RDFSLabel, s"$partLabel which is part of a $wholeLabel")
    axioms
  }

  val input = new File(args(0))
  val output = new File(args(1))

  val axioms = Source.fromFile(input, "utf-8").getLines.flatMap(convertLine).toSet
  val manager = OWLManager.createOWLOntologyManager()
  val ontology = manager.createOntology(axioms.asJava)
  manager.saveOntology(ontology, IRI.create(output))

}