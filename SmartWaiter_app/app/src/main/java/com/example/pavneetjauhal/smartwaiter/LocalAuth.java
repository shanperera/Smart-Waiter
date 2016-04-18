package com.example.pavneetjauhal.smartwaiter;

import android.util.Base64;
import android.util.Log;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/*
  Created by pavneetjauhal on 16-01-26.

  Class to do all of the user authentication for the app.

 */
public class LocalAuth {
    /* This will be stored in the database as well */
    public final String password;
    public final String salt;

    public LocalAuth(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }

    /* Check if user input matches stored password.*/
    public static boolean checkPassword(String password, String stored, String salt) throws Exception {
        // Log.d("FACKKK2", salt);
        byte[] decodedPass = Base64.decode(salt, Base64.DEFAULT);
        String text = new String(decodedPass, "UTF-8");
        Log.d("FACKKK2-C", stored);
        return password.equals(stored);
    }

    /* Computes a salted hash of given plaintext password. However,
       we will not be encoding the pass here. Tried encoding password, but
       code makes it platform specific */
    public static LocalAuth computeSaltedHash(String password) throws Exception {
        //length of salt
        byte[] pass = password.getBytes("UTF-8");
        String base64 = Base64.encodeToString(pass, Base64.DEFAULT);
        Log.d("FACKKK2-P", base64);
        //Log.d("FACKKK2-P", );
        return new LocalAuth(password, base64);
    }

    /*  PBKDF2 hash used to store passwords  */
    private static String computeHashValue(String password, byte[] salt) throws Exception {
        /* Need to throw UI exception here */
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Password too short");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        int hashIteration = 20000;
        int keyLen = 256;
        SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, hashIteration, keyLen));
        return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    }
}
