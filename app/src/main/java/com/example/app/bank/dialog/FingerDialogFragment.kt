package com.example.app.bank.dialog

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.app.bank.R
import com.example.app.bank.base.BaseDialog
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

class FingerDialogFragment : BaseDialog() {
    companion object {
        @Volatile
        private var isShowing: Boolean = false
    }

    private val KEY_NAME = "yourKey"
    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var cryptoObject: FingerprintManager.CryptoObject
    private lateinit var fingerprintManager: FingerprintManager
    private lateinit var keyguardManager: KeyguardManager
    internal var onDismissDialog: () -> Unit = {}
    internal var onReplaceFragment: () -> Unit = {}
    private lateinit var fingerDialogFragment: FingerDialogFragment

    override fun setContentDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.layout_fingerprint_success)
    }

    override fun initListeners(dialog: Dialog) {
        val tvDialog = dialog.findViewById<TextView>(R.id.tvClose)
        tvDialog.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fingerDialogFragment = FingerDialogFragment()
        context?.let {
            keyguardManager = it.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            fingerprintManager = it.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
        }

        keyStore = KeyStore.getInstance("AndroidKeyStore")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (initCipher()) {
            cryptoObject = FingerprintManager.CryptoObject(cipher)
            context?.let {
                FingerprintHandler(it).apply {
                    fingerprintManager.let {
                        startAuth(it, cryptoObject)

                    }
                    this.onFingerprintSuccess = {
                        onReplaceFragment()
                    }
                }
            }
        }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        if (!isShowing) {
            isShowing = true
            super.show(manager, tag)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if (isShowing) {
            isShowing = false
            super.onDismiss(dialog)
            onDismissDialog()
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    fun initCipher(): Boolean {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )

        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        try {
            keyStore.load(null)
            val key = keyStore.getKey(KEY_NAME, null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE, key)
            //Return true if the cipher has been initialized successfully//
            return true
        } catch (@SuppressLint("NewApi") e: KeyPermanentlyInvalidatedException) {

            //Return false if cipher initialization failed//
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }
}
