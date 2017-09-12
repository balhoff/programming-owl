package org.renci.owl

import org.phenoscape.scowl._

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

}