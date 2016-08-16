package com.github.cdclaxton.OneTimePad

/**
  * Example application to encrypt and decrypt a message.
  */
object EncryptDecryptApp {

  def main(args: Array[String]): Unit = {

    // Generate a random one-time pad
    val pad: Map[Char, Char] = OneTimePad.genPad

    // Encrypt a given string
    val stringToEncrypt = "Hello! This is an example message to encrypt."
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
