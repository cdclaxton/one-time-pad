package com.github.cdclaxton.OneTimePad

import org.scalacheck.{Gen, Prop, Properties}
import org.scalacheck.Prop.{BooleanOperators, AnyOperators}

/**
  * Property-based tests for OneTimePad.
  */
object OneTimePadTest extends Properties("OneTimePad") {

  val oneTimePadGen = for {
    pad <- Gen.oneOf(Seq(OneTimePad.genPad))
  } yield pad

  property("pad generator") = Prop.forAll(oneTimePadGen) { pad: Map[Char,Char] =>
    pad.keys.toSet == pad.values.toSet
  }

  property("pad inversion") = Prop.forAll { (cs: Set[Char], is: Set[Int]) =>
    val minLength: Int = math.min(cs.size, is.size)
    val keys = cs.toList.take(minLength)
    val values = is.map(_.toChar).toList.take(minLength)
    val m = keys.zip(values).toMap
    val invertedM = OneTimePad.invertPad(m)
    (m.keys.toSet == invertedM.values.toSet) && (m.values.toSet == invertedM.keys.toSet)
  }

  val validCharGen: Gen[Char] = for {
    c <- Gen.oneOf(OneTimePad.validChars)
  } yield c

  val validCharStringGen: Gen[String] = for {
    s <- Gen.listOf(validCharGen)
  } yield s.mkString

  property("transform on valid characters") = Prop.forAll(validCharStringGen) { str: String =>
    val f = (x: Char) => x // Identity transformation
    OneTimePad.transform(str, f) == str
  }

  property("transform on any characters") = Prop.forAll { str: String =>
    str.length > 0 ==> {
      val f = (x: Char) => x // Identity transformation
      val transformed: String = OneTimePad.transform(str, f)
      val setOfTransformedChars: Set[Char] = transformed.toList.toSet
      val setOfValidChars: Set[Char] = OneTimePad.validChars.toSet
      setOfTransformedChars.forall(c => setOfValidChars.contains(c) || c == OneTimePad.unknownChar)
    }
  }

  property("encrypt and decrypt characters") = Prop.forAll(validCharGen) { c: Char =>
    val pad = OneTimePad.genPad
    OneTimePad.decrypt(pad)(OneTimePad.encrypt(pad)(c)) == c
  }

  property("encrypt and decrypt strings with valid chars") = Prop.forAll(validCharStringGen) { s: String =>
    val pad = OneTimePad.genPad
    val encrypted = OneTimePad.encrypt(s, pad)
    OneTimePad.decrpyt(encrypted, pad) =? s
  }

}
