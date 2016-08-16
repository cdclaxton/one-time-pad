package com.github.cdclaxton.OneTimePad

/**
  * Example encoder and decoder.
  */
object EncodeDecodeApp {

  def main(args: List[String]): Unit = {

    // Generate a random one-time pad
    val pad: Map[Char, Char] = OneTimePad.genPad

    // Encrypt a given string
    val stringToEncrypt = "Hello! This is an example to encrypt."
    val encrypted = OneTimePad.encrypt(stringToEncrypt, pad)

    // Decrypt the string
    val decrypted = OneTimePad.decrpyt(encrypted, pad)

    // Display
    println(
      s"""String to encrypt: $stringToEncrypt
         |Encrypted string:  $encrypted
         |Decrypted string:  $decrypted""".stripMargin)

  }

}
