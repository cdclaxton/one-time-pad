package com.github.cdclaxton.OneTimePad

import org.scalacheck.{Gen, Prop, Properties}

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

  property("encrypt and decrypt characters") = Prop.forAll(validCharGen) { c: Char =>
    val pad = OneTimePad.genPad
    OneTimePad.decrypt(pad)(OneTimePad.encrypt(pad)(c)) == c
  }

  property("encrypt and decrypt strings") = Prop.forAll { (s: String) =>
    val pad = OneTimePad.genPad
    OneTimePad.decrpyt(OneTimePad.encrypt(s, pad), pad) == s
  }

}
