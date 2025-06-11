package org.example.demo

import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import javax.crypto.spec.GCMParameterSpec


class Greeting {
    private val platform = getPlatform()

    fun greet(): String {

        val encryptMessage = encryptMessage(
            "Your secret message",
            loadPublicKey("/Users/jozefdransfield/Workspace/encrypto/demo/public_key.pem")
        )
        return decryptMessage(encryptMessage, loadPrivateKey("/Users/jozefdransfield/Workspace/encrypto/demo/private_key.pem"))

    }
}

@OptIn(ExperimentalEncodingApi::class)
fun loadPublicKey(path: String) : PublicKey {
    val key = String(Files.readAllBytes(Paths.get(path))).replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\\s".toRegex(), "")

    val decoded: ByteArray = Base64.decode(key)
    val spec = X509EncodedKeySpec(decoded)
    val keyFactory = KeyFactory.getInstance("RSA")
    return keyFactory.generatePublic(spec)
}

@OptIn(ExperimentalEncodingApi::class)
fun loadPrivateKey(path: String) : PrivateKey {
    val key = String(Files.readAllBytes(Paths.get(path))).replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("\\s".toRegex(), "")

    val decoded: ByteArray = Base64.decode(key)
    val spec = PKCS8EncodedKeySpec(decoded)
    val keyFactory = KeyFactory.getInstance("RSA")
    return keyFactory.generatePrivate(spec)

}

@OptIn(ExperimentalEncodingApi::class)
fun encryptMessage(message: String, publicKey: PublicKey): String {
    // Generate a random AES key
    val keyGen = KeyGenerator.getInstance("AES")
    keyGen.init(256)
    val aesKey = keyGen.generateKey()

    // Encrypt the actual message with AES
    val aesCipher = Cipher.getInstance("AES/GCM/NoPadding")
    aesCipher.init(Cipher.ENCRYPT_MODE, aesKey)
    val iv = aesCipher.iv  // Get the IV
    val encryptedMessage = aesCipher.doFinal(message.toByteArray())

    // Encrypt the AES key with RSA
    val rsaCipher = Cipher.getInstance("RSA")
    rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey)
    val encryptedAesKey = rsaCipher.doFinal(aesKey.encoded)

    // Combine encrypted AES key, IV, and encrypted message
    val combined = ByteArray(encryptedAesKey.size + iv.size + encryptedMessage.size)
    var offset = 0
    System.arraycopy(encryptedAesKey, 0, combined, offset, encryptedAesKey.size)
    offset += encryptedAesKey.size
    System.arraycopy(iv, 0, combined, offset, iv.size)
    offset += iv.size
    System.arraycopy(encryptedMessage, 0, combined, offset, encryptedMessage.size)

    return Base64.encode(combined)
}

@OptIn(ExperimentalEncodingApi::class)
fun decryptMessage(encryptedMessage: String, privateKey: PrivateKey): String {
    // Decode the Base64 combined message
    val combined = Base64.decode(encryptedMessage)

    // Extract the encrypted AES key (first 384 bytes for 3072-bit RSA)
    val encryptedAesKeySize = 384
    val ivSize = 12  // GCM typically uses 12 bytes IV
    
    val encryptedAesKey = ByteArray(encryptedAesKeySize)
    val iv = ByteArray(ivSize)
    val encryptedData = ByteArray(combined.size - encryptedAesKeySize - ivSize)
    
    var offset = 0
    System.arraycopy(combined, offset, encryptedAesKey, 0, encryptedAesKeySize)
    offset += encryptedAesKeySize
    System.arraycopy(combined, offset, iv, 0, ivSize)
    offset += ivSize
    System.arraycopy(combined, offset, encryptedData, 0, encryptedData.size)

    // Decrypt the AES key using RSA private key
    val rsaCipher = Cipher.getInstance("RSA")
    rsaCipher.init(Cipher.DECRYPT_MODE, privateKey)
    val decryptedAesKey = rsaCipher.doFinal(encryptedAesKey)

    // Recreate the AES key
    val aesKey: SecretKey = SecretKeySpec(decryptedAesKey, "AES")

    // Decrypt the actual message using the AES key and IV
    val aesCipher = Cipher.getInstance("AES/GCM/NoPadding")
    val gcmSpec = GCMParameterSpec(128, iv)  // 128-bit authentication tag
    aesCipher.init(Cipher.DECRYPT_MODE, aesKey, gcmSpec)
    val decryptedMessage = aesCipher.doFinal(encryptedData)

    return String(decryptedMessage)
}