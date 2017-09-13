# Programming OWL with Scala and Scowl

## Scala
[Scala](http://www.scala-lang.org) is a programming language that runs on the Java Virtual Machine (JVM). The language emphasizes a fusion of object-oriented and functional programming styles. The name "Scala" comes from "scalable language". It is meant to scale not just in terms of computing power but also from quick scripting development to complex systems. Scala has several features which make it a good fit for programmatic manipulation of OWL ontologies:

- **JVM language.** Since Scala runs on the JVM, Java libraries can be used transparently and with full performance. The majority of OWL and semantic web toolkits are developed in Java.
- **Reduced boilerplate.** Scala is statically typed like Java, however the compiler is able to infer the types of expressions, allowing the programmer to dispense with many type annotations. This results in code that can look more like Python than Java, with all the benefits of compile-time type-checking.
- **Functional programming support.** Higher order functions (like `list.map()`) are a lot more fun than writing loops. Some of this was added to Java 8, but it's much nicer in Scala.
- **Flexible syntax.** Operator overloading, symbolic method names, optional parentheses, implicit arguments, and other features make Scala a powerful language for constructing libraries which act like embedded domain-specific languages (DSLs).

## Scowl
[Scowl](https://github.com/phenoscape/scowl) is an OWL DSL implemented in Scala. It wraps functionality provided by the Java [OWL API](http://owlapi.sourceforge.net). Scowl makes it possible to embed complex OWL class expressions and axioms within program code. OWL axioms written using Scowl are declarative and easy to read. Two OWL styles are supported: [Manchester syntax](https://www.w3.org/TR/owl2-manchester-syntax/) and [Functional syntax](https://www.w3.org/TR/owl2-syntax/).

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

Here is the same code written using Scala and Scowl, in Manchester style:

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

Scowl doesn't create its own objects. The return values for the above methods are all the same `OWLClass`es and `OWLAxiom`s that you get from the `OWLDataFactory`.

## Using Scowl
### Imports
Adding an `import org.phenoscape.scowl._` will bring in the entire Scowl syntax. That's usually easier than importing requirements one-by-one, especially since most Scowl functionality is provided *implicitly*, rather than directly referenced in your code.

### Infix notation
While it may not look like it, when you're using the Scowl Manchester style, you are just calling methods and passing arguments. In Scala, you can optionally replace the method-calling punctuation (`.()`) with spaces. In fact, this is how operators are implemented: addition is simply a method named `+`. Rather than `1 + 2`, you could also write `1.+(2)`. A more standard method can also be called this way: `"hello".split("ll")` also works as `"hello" split "ll"`. Although this is not really encouraged for most alphabetic-named methods, it is a great fit for embedded DSLs.

### Extractors
The Scowl functional syntax implements a method called `unapply`, which acts like the reverse of a constructor: it takes an object and tries to give back the arguments used to create the object. Objects with `unapply` methods are called "extractors". They can be used within Scala pattern-matching and can be quite convenient:

```scala
def handleClassExpression(cls: OWLClassExpression): Unit = cls match {
  case Class(iri)                     => println(s"Named class with IRI $iri")
  case ObjectIntersectionOf(operands) => println(s"Intersection with ${operands.size} operands")
  case _                              => println("Expression type not supported")
}
```

Variable names introduced in the "constructor" on the left side are like holes that can be filled in by values that match the shape of the constructor. This example also demonstrates the very handy Scala [string-interpolation](http://docs.scala-lang.org/overviews/core/string-interpolation.html) syntax.

Extractors can also be used within "for comprehensions", which are kind of a super-powered version of Python's list comprehensions:

```scala
import scala.collection.JavaConverters._
val ontology = OWLManager.createOWLOntologyManager().loadOntology(IRI.create("http://purl.obolibrary.org/obo/zfa.owl"))

val texts = for {
  SubClassOf(_, subclass, ObjectSomeValuesFrom(property, filler)) <- ontology.getAxioms().asScala
} yield {
  s"$property $filler"
}
```

Here we convert all the properties and filler class expressions used in existential restrictions in ZFA into a set of string representations. Importing the converters from `JavaConverters` lets us call `asScala` on Java collections, to make them work in Scala for-comprehensions. The first part of the comprehension is a *generator*, which takes items from the set of axioms on the right of the `<-`, one by one. If the item matches the extractor pattern on the left, the variable values are used in the *yield* expression to create an item in the new collection which is stored in `texts`.

### Learning Scowl syntax
The best way to get an idea of how various class expressions and axioms are written in Scowl is to look through the examples provided with the distribution. All of the constructs from the [OWL 2 Web Ontology Language Primer](https://www.w3.org/TR/owl2-primer/) are implemented using both the [Manchester style](https://github.com/phenoscape/scowl/blob/master/src/main/scala/org/phenoscape/scowl/example/OWL2PrimerManchester.scala) and the [Functional style](https://github.com/phenoscape/scowl/blob/master/src/main/scala/org/phenoscape/scowl/example/OWL2PrimerFunctional.scala).

Scowl is quite useful for legibly generating OWL axioms from tables of input data. Here's a [tiny demonstration script](https://github.com/balhoff/programming-owl/blob/master/src/main/scala/org/renci/owl/ConvertTableToOWL.scala).

## Start using Scala
The best way to start using Scala is to install [SBT](http://www.scala-sbt.org), the Scala build tool. If you're on a Mac and use [Homebrew](https://brew.sh) to install command-line tools, it's as simple as `brew install sbt`. There are also directions for [Windows](http://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Windows.html) and [Linux](http://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html). Basic SBT project definitions are quite simple. Here is [build.sbt](https://github.com/balhoff/programming-owl/blob/master/build.sbt) from this demo repository:

```scala

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

```

If you have `sbt` installed, and have cloned [this project from GitHub](https://github.com/balhoff/programming-owl), you can run the table conversion demonstration script at the terminal: `sbt "run parts.tsv parts.owl"`
