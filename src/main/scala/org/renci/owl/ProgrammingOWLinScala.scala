package org.renci.owl

import org.phenoscape.scowl._
import org.semanticweb.owlapi.model.OWLClassExpression
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.IRI

object ProgrammingOWLinScala {

  val hasParent = ObjectProperty("http://www.co-ode.org/roberts/family-tree.owl#hasParent")
  val isParentOf = ObjectProperty("http://www.co-ode.org/roberts/family-tree.owl#isParentOf")
  val isSiblingOf = ObjectProperty("http://www.co-ode.org/roberts/family-tree.owl#isSiblingOf")
  val Person = Class("http://www.co-ode.org/roberts/family-tree.owl#Person")
  val FirstCousin = Class("http://www.co-ode.org/roberts/family-tree.owl#FirstCousin")

  val axiom = FirstCousin EquivalentTo (Person and (hasParent some (Person and (isSiblingOf some (Person and (isParentOf some Person))))))

  val axiomFunctional = EquivalentClasses(FirstCousin, ObjectIntersectionOf(
    Person, ObjectSomeValuesFrom(hasParent, ObjectIntersectionOf(
      Person, ObjectSomeValuesFrom(isSiblingOf, ObjectIntersectionOf(
        Person, ObjectSomeValuesFrom(isParentOf, Person)))))))

  1 + 2

  1.+(2)

  "hello".split("ll")

  "hello" split "ll"

  def handleClassExpression(cls: OWLClassExpression): Unit = cls match {
    case Class(iri)                     => println(s"Named class with IRI $iri")
    case ObjectIntersectionOf(operands) => println(s"Intersection with ${operands.size} operands")
    case _                              => println("Expression type not supported")
  }

  import scala.collection.JavaConverters._
  val ontology = OWLManager.createOWLOntologyManager().loadOntology(IRI.create("http://purl.obolibrary.org/obo/zfa.owl"))

  val text = for {
    SubClassOf(_, subclass, ObjectSomeValuesFrom(property, filler)) <- ontology.getAxioms().asScala
  } yield {
    s"$property $filler"
  }

}