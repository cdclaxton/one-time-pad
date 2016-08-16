package com.github.cdclaxton.OneTimePad

import scala.util.Random

/**
  * Toy One-time pad generator
  */
object OneTimePad {

  /**
    * List of characters that can be encoded and decoded
    */
  val validChars: List[Char] = ('a' to 'z').toList ::: (0 to 9).toList.map(_.toChar)

  val unknownChar: Char = '.'

  /**
    * Generate a one-time pad.
    * Key represents the character to encode and value represents the encoded character.
    * @return
    */
  def genPad: Map[Char,Char] = {
    val randomised: List[Char] = Random.shuffle(validChars)
    validChars.zip(randomised).toMap
  }

  /**
    * Invert a one-time pad (i.e. invert the keys and values).
    * @param m  One-time pad
    * @return   Inverted one-time pad
    */
  def invertPad(m: Map[Char, Char]): Map[Char, Char] = {
    m.values.zip(m.keys).toMap
  }

  /**
    * Encode a character given a one-time pad.
    * @param c    Character to encode
    * @param pad  One-time pad to use
    * @return     Encoded character
    */
  def encrypt(pad: Map[Char,Char])(c: Char): Char = {
    require(pad.contains(c), s"Cannot encode $c")
    pad(c)
  }

  /**
    * Decode an encrypted character given a one-time pad.
    * @param c    Character to decode
    * @param pad  One-time pad
    * @return     Decoded character
    */
  def decrypt(pad: Map[Char,Char])(c: Char): Char = {
    require(pad.values.toSet.contains(c), s"Cannot decode $c")
    invertPad(pad)(c)
  }

  /**
    * Transform the characters in a string using the specified function.
    * @param str  String to transform
    * @param f    Function that maps characters
    * @return     Transformed string
    */
  def transform(str: String, f: (Char) => Char): String = {
    str
      .toLowerCase
      .toList
      .map{ case (c: Char) => if (validChars.contains(c)) f(c) else unknownChar }
      .mkString
  }

  /**
    * Encrypt a string given a one-time pad to use.
    * @param str  String to encrypt
    * @param pad  One-time pad
    * @return     Encrypted string
    */
  def encrypt(str: String, pad: Map[Char,Char]): String = {
    transform(str, encrypt(pad))
  }

  /**
    * Decrypt a string given a one-time pad to use.
    * @param str  String to encrypt
    * @param pad  One-time pad
    * @return     Decrypted string
    */
  def decrpyt(str: String, pad: Map[Char,Char]): String = {
    transform(str, decrypt(pad))
  }

}
