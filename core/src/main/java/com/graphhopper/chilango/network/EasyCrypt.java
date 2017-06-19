package com.graphhopper.chilango.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Alexander Gr
 * @see http://blog.axxg.de
 *
 */
public class EasyCrypt {

	private Key key = null;
	private String cryptoType = null;
	private Cipher outputCipher;
	private Cipher inputCipher;
	public static final String aes="AES/CBC/PKCS5Padding";

	/**
	 * @param Key
	 *            securityKey
	 * @param verfahren
	 *            bestimmt das verwendete Verschluesselungsverfahren "RSA",
	 *            "AES", ....
	 * @throws Exception
	 */
	public EasyCrypt(Key k, String type) throws Exception {
		this.key = k;
		this.cryptoType = aes;
	}

	public void writeGeneratedAESKey(File file) {
		// Datei

		// zufaelligen Key erzeugen
		KeyGenerator keygen;
		try {
			keygen = KeyGenerator.getInstance("AES");

			keygen.init(128);
			Key key = keygen.generateKey();

			// Key in die Datei schreiben
			byte[] bytes = key.getEncoded();
			FileOutputStream keyfos = new FileOutputStream(file);
			keyfos.write(bytes);
			keyfos.close();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Key readKey(File file) {
		// Datei
		 
		// Key lesen
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);

		byte[] encodedKey = new byte[(int) file.length()];
		fis.read(encodedKey);
		fis.close();
		       
		// generiere Key
		Key newKey = new SecretKeySpec(encodedKey, "AES");
		
		return newKey;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Verschluesselt einen Outputstream
	 * 
	 * @param os
	 *            Klartext-Outputstream
	 * @return verschluesselter Outputstream
	 * @throws Exception
	 */
	public OutputStream encryptOutputStream(OutputStream os) throws Exception {
		// integritaet pruefen
		valid();

		// eigentliche Nachricht mit RSA verschluesseln
		outputCipher = Cipher.getInstance(cryptoType);
		
		System.out.println("Block size: "+outputCipher.getBlockSize());
		outputCipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(new byte[16]));
		os = new CipherOutputStream(os, outputCipher);

		return os;
	}
	
	public Cipher getOuputCipher(){return outputCipher;}
	public Cipher getInputCipher(){return inputCipher;}

	/**
	 * Entschluesselt einen Inputstream
	 * 
	 * @param is
	 *            verschluesselter Inputstream
	 * @return Klartext-Inputstream
	 * @throws Exception
	 */
	public InputStream decryptInputStream(InputStream is) throws Exception {
		// integritaet pruefen
		valid();

		// Daten mit AES entschluesseln
		inputCipher = Cipher.getInstance(cryptoType);
		inputCipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(new byte[16]));
		is = new CipherInputStream(is, inputCipher);

		return is;
	}

	/**
	 * Verschluesselt einen Text in BASE64
	 * 
	 * @param text
	 *            Klartext
	 * @return BASE64 String
	 * @throws Exception
	 */
	public String encrypt(String text) throws Exception {
		// integritaet pruefen
		valid();

		// Verschluesseln
		Cipher cipher = Cipher.getInstance(cryptoType);
		cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(new byte[16]));
		byte[] encrypted = cipher.doFinal(text.getBytes());

		// bytes zu Base64-String konvertieren
		BASE64Encoder myEncoder = new BASE64Encoder();
		String geheim = myEncoder.encode(encrypted);

		return geheim;
	}

	/**
	 * Entschluesselt einen BASE64 kodierten Text
	 * 
	 * @param geheim
	 *            BASE64 kodierter Text
	 * @return Klartext
	 * @throws Exception
	 */
	public String decrypt(String message) throws Exception {
		// integritaet pruefen
		valid();

		// BASE64 String zu Byte-Array
		BASE64Decoder myDecoder = new BASE64Decoder();
		byte[] crypted = myDecoder.decodeBuffer(message);

		// entschluesseln
		Cipher cipher = Cipher.getInstance(cryptoType);
		cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(new byte[16]));
		byte[] cipherData = cipher.doFinal(crypted);
		return new String(cipherData);
	}

	public SealedObject encrypt(Serializable inputObject) throws Exception {
		SealedObject sealedObject = null;

		try {
			Cipher cipher = Cipher.getInstance(cryptoType);
			cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(new byte[16]));
			sealedObject = new SealedObject(inputObject, cipher);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return sealedObject;
	}

	public Serializable decrypt(SealedObject object) throws Exception {

		Serializable serializedObject = null;

		try {
			Cipher cipher = Cipher.getInstance(cryptoType);
			cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(new byte[16]));
			serializedObject = (Serializable) object.getObject(cipher);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return serializedObject;
	}

	// ++++++++++++++++++++++++++++++
	// Validation
	// ++++++++++++++++++++++++++++++

	private boolean valid() throws Exception {
		if (cryptoType == null) {
			throw new NullPointerException("no cryptoType!");
		}

		if (key == null) {
			throw new NullPointerException("no key!");
		}

		if (cryptoType.isEmpty()) {
			throw new NullPointerException("no cryptoType!");
		}

		return true;
	}

	// ++++++++++++++++++++++++++++++
	// Getter Setter
	// ++++++++++++++++++++++++++++++

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getCryptoType() {
		return cryptoType;
	}

	public void setCryptoType(String type) {
		this.cryptoType = type;
	}
}
