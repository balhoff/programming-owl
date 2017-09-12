package org.renci.owl;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

public class ProgrammingOWLinJava {

	OWLDataFactory factory = OWLManager.getOWLDataFactory();

	OWLObjectProperty hasParent = factory.getOWLObjectProperty(IRI.create("http://www.co-ode.org/roberts/family-tree.owl#hasParent"));
	OWLObjectProperty isParentOf = factory.getOWLObjectProperty(IRI.create("http://www.co-ode.org/roberts/family-tree.owl#isParentOf"));
	OWLObjectProperty isSiblingOf = factory.getOWLObjectProperty(IRI.create("http://www.co-ode.org/roberts/family-tree.owl#isSiblingOf"));
	OWLClass Person = factory.getOWLClass(IRI.create("http://www.co-ode.org/roberts/family-tree.owl#Person"));
	OWLClass FirstCousin = factory.getOWLClass(IRI.create("http://www.co-ode.org/roberts/family-tree.owl#FirstCousin"));

	OWLAxiom axiom = factory.getOWLEquivalentClassesAxiom(FirstCousin, factory.getOWLObjectIntersectionOf(
			Person, factory.getOWLObjectSomeValuesFrom(hasParent, factory.getOWLObjectIntersectionOf(
					Person, factory.getOWLObjectSomeValuesFrom(isSiblingOf, factory.getOWLObjectIntersectionOf(
							Person, factory.getOWLObjectSomeValuesFrom(isParentOf, Person)))))));


}
