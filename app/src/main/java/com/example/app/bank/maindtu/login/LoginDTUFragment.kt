package com.example.app.bank.maindtu.login


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.KeyguardManager
import android.content.Context.FINGERPRINT_SERVICE
import android.content.Context.KEYGUARD_SERVICE
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.example.app.bank.dialog.FingerDialogFragment
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.visible
import com.example.app.bank.maindtu.home.HomeFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login_app.tvCheckPassword
import java.security.KeyStore
import javax.crypto.KeyGenerator

//KeyguardManager is Class that can be used to lock and unlock the keyguard.
//FingerprintManager.CryptoObject?A wrapper class for the crypto objects supported by FingerprintManager.

class LoginDTUFragment : BaseFragment() {

    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var fingerprintManager: FingerprintManager
    private lateinit var keyguardManager: KeyguardManager
    private lateinit var fingerDialogFragment: FingerDialogFragment
    private lateinit var viewModel: LoginDTUViewModel
    private lateinit var auth: FirebaseAuth
    private var firebase = FirebaseDatabase.getInstance().reference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            keyguardManager = it.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            fingerprintManager = it.getSystemService(FINGERPRINT_SERVICE) as FingerprintManager
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            viewModel =
                LoginDTUViewModel(keyguardManager, fingerprintManager, keyStore, keyGenerator, LocalRepository())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                viewModel.checkFingerprint(it)
            }
            fingerDialogFragment = FingerDialogFragment()
            initView()
            initListener()
            edtUserName.setText("thanhtunga0000@gmail.com")
            edtPasswordDTU.setText("Thanhtung123123")
        }
        FirebaseApp.initializeApp(context)
        auth = FirebaseAuth.getInstance()
    }

    private fun initView() {
        btnLogin.setOnClickListener {
            replaceFragment(HomeFragment(), true)
        }
    }

    override fun onBindViewModel() {
        addDisposables(
            viewModel.stateCheckFinger
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        handleCheckfingerPrintSuccess()
                    }
                }, {}),
            viewModel.loadingSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it)
                        containerProgressbarLogin.visible()
                    else {
                        containerProgressbarLogin.gone()
                    }
                }, {}),
            viewModel.listUserSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {})
        )
    }

    private fun initListener() {
        btnLogin.setOnClickListener {
            val email = edtUserName.text.toString().trim()
            val password = edtPasswordDTU.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                tvCheckPassword.text = "Please enter email!"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                tvCheckPassword.text = "Please enter password!"
                return@setOnClickListener
            }

            signInWithEmailAndPassword(email, password)
        }
    }

    private fun signInWithEmailAndPassword(email: String, passWord: String) {
        context?.let { ctx ->
            containerProgressbarLogin.visible()
            auth.signInWithEmailAndPassword(email, passWord)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        handePostUIDUser(email)
                    } else {
                        containerProgressbarLogin.gone()
                        if (!viewModel.isInternetAvailable(ctx)) {
                            containerProgressbarLogin.gone()
                            tvCheckPassword.text = ctx.getString(R.string.tv_check_connect_internet)
                        }
                    }
                }

        }
    }


    private fun handleCheckfingerPrintSuccess() {
        Handler().postDelayed({
            fingerDialogFragment.apply {
                this.onReplaceFragment = {
                    replaceFragment(HomeFragment(), true)
                }
            }.show(childFragmentManager, FingerDialogFragment::class.java.simpleName)

        }, 1000)
    }

    @SuppressLint("CheckResult")
    private fun handePostUIDUser(email: String) {
        viewModel.getUserList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                for (i in 0 until it.size) {
                    if (it[i].email == email) {
                        val userUpdates = HashMap<String, String>()
                        val usersRef = firebase.child("listUser")
                        auth.uid?.let { uid ->
                            userUpdates["${it[i].key}/id"] = uid
                            //getUser position
                            it[i].run {
                                val user = User(
                                    id = uid,
                                    key = key,
                                    name = name,
                                    avatar = avatar,
                                    moneyBorrow = moneyBorrow,
                                    debtpaymentplan = debtpaymentplan,
                                    assettax = assettax,
                                    totalasset = totalasset,
                                    address = address,
                                    sex = sex,
                                    email = this.email,
                                    phone = phone,
                                    cmnd = cmnd,
                                    interest = interest,
                                    timeBorrow = timeBorrow
                                )
                                replaceFragment(HomeFragment.newInstance(user), true)
                            }
                        }
                        usersRef.updateChildren(userUpdates as Map<String, Any>)
                    }
                }
            }, {})
    }
}

