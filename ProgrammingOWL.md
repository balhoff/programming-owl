# Programming OWL with Scala and Scowl

## Scala
Scala is a programming language that runs on the Java Virtual Machine (JVM). The language emphasizes a fusion of object-oriented and functional programming styles. The name "Scala" comes from "scalable language". It is meant to scale not just in terms of computing power but also from quick scripting development to complex systems. Scala has several features which make it a good fit for programmatic manipulation of OWL ontologies:

- **JVM language.** Since Scala runs on the JVM, Java libraries can be used transparently and with full performance. The majority of OWL and semantic web toolkits are developed in Java.
- **Reduced boilerplate.** While Scala is statically typed, like Java, the compiler is able to infer the types of expressions, allowing the programmer to dispense with many type annotations. This results in code that can look more like Python than Java, with all the benefits of compile-time type-checking.
- **Flexible syntax.** Operator overloading, symbolic method names, optional parentheses, implicit arguments, and other features make Scala a powerful language for constructing libraries which act like embedded domain-specific languages (DSLs).

## Scowl
Scowl is an OWL DSL implemented in Scala. It wraps functionality provided by the Java OWL API. Scowl makes it possible to embed complex OWL class expressions and axioms within program code. OWL axioms written using Scowl are declarative and easy to read. Two OWL styles are supported: Manchester syntax and Functional syntax.

## Code comparison
Here is a complex axiom written in Java with the OWL API:

```java
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
```

Here is the same code written using Scala and Scowl:

```scala
val hasParent = ObjectProperty("http://www.co-ode.org/roberts/family-tree.owl#hasParent")
val isParentOf = ObjectProperty("http://www.co-ode.org/roberts/family-tree.owl#isParentOf")
val isSiblingOf = ObjectProperty("http://www.co-ode.org/roberts/family-tree.owl#isSiblingOf")
val Person = Class("http://www.co-ode.org/roberts/family-tree.owl#Person")
val FirstCousin = Class("http://www.co-ode.org/roberts/family-tree.owl#FirstCousin")

val axiom = FirstCousin EquivalentTo (Person and (hasParent some (Person and (isSiblingOf some (Person and (isParentOf some Person))))))
```
or, in Functional style:

```scala
val axiomFunctional = EquivalentClasses(FirstCousin, ObjectIntersectionOf(
    Person, ObjectSomeValuesFrom(hasParent, ObjectIntersectionOf(
      Person, ObjectSomeValuesFrom(isSiblingOf, ObjectIntersectionOf(
        Person, ObjectSomeValuesFrom(isParentOf, Person)))))))
```
