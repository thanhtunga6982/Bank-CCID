package com.example.app.bank.maindtu.login

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.net.ConnectivityManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.example.app.appbank2.exception.FingerprintException
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.KeyGenerator

class LoginDTUViewModel(
    var keyguardManager: KeyguardManager, var fingerprintManager: FingerprintManager,
    var keyStore: KeyStore, var keyGenerator: KeyGenerator,
    var localRepository: LocalRepository
) {
    private val KEY_NAME = "yourKey"
    internal var listUser = mutableListOf<User>()
    internal var listUserSubject = BehaviorSubject.create<MutableList<User>>()
    internal var loadingSubject = BehaviorSubject.create<Boolean>()
    internal val stateCheckFinger = BehaviorSubject.create<Boolean>()

    init {
        stateCheckFinger.onNext(false)
    }

    @SuppressLint("CheckResult")
    fun getUserList(): Single<MutableList<User>> = localRepository.getUser()
        .subscribeOn(Schedulers.io())
        .doOnSuccess {
            listUser.clear()
            listUser.addAll(it)
            listUserSubject.onNext(listUser)
            println("xxxxxListUser$it")
        }
        .doOnSubscribe {
            loadingSubject.onNext(true)
        }
        .doFinally {
            loadingSubject.onNext(false)
        }
        .doOnError {
        }


    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkFingerprint(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.let {
                keyguardManager = it.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                fingerprintManager = it.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
                if (!fingerprintManager.isHardwareDetected) {
                    // Device doesn't support fingerprint authentication
                    Toast.makeText(it, "Điện thoại của bạn không hỗ trợ xác thực vân ", Toast.LENGTH_LONG).show()
                    return false
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    // User hasn't enrolled any fingerprints to authenticate with
                    Toast.makeText(it, "Chưa đăng ký vân tay ", Toast.LENGTH_LONG).show()
                    return false
                } else {
                    stateCheckFinger.onNext(true)
                }
                //Check permission use fingerprint
                if (ActivityCompat.checkSelfPermission(
                        it,
                        android.Manifest.permission.USE_FINGERPRINT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return false
                }
                //Check that the lockscreen is secured
                if (!keyguardManager.isKeyguardSecure) {
                    return false
                    // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
                } else {
                    generateKey()
                    try {
                        // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
                        keyStore = KeyStore.getInstance("AndroidKeyStore")
                        //Generate the key
                        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                    } catch (e: FingerprintException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return true
    }

//    fun getToken(){
//        localRepository.getToken()
//    }

    fun setToken(token: String){
        localRepository.setToken(token)
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(FingerprintException::class)
    private fun generateKey() {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            //Generate the key
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            //Initialize an empty KeyStore//
            keyStore.load(null)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_PKCS7
                    ).build()
            )
            //Generate the key//
            keyGenerator.generateKey()
        } catch (exc: KeyStoreException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: NoSuchAlgorithmException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: NoSuchProviderException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: InvalidAlgorithmParameterException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: CertificateException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: IOException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        }
    }
}