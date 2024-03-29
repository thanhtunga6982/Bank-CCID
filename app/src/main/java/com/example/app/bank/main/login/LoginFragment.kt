package com.example.app.bank.main.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.AppConstant
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.visible
import com.example.app.bank.main.condition.ConditionBorrowMoney
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login_app.*


class LoginFragment : BaseFragment(), View.OnTouchListener {

    private lateinit var viewModel: LoginFragmentViewModel
    private lateinit var auth: FirebaseAuth
    private var firebase = FirebaseDatabase.getInstance().reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context?.let {
            viewModel = LoginFragmentViewModel(LocalRepository(it))
        }
        auth = FirebaseAuth.getInstance()

        return inflater.inflate(R.layout.fragment_login_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserList()
        edtName.setText("thanhtunga0000@gmail.com")
        edtPassword.setText("Thanhtung123123")
        initListener()
    }

    override fun onBindViewModel() {
        addDisposables(
            viewModel.loadingSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it)
                        containerProgressbar.visible()
                    else {
                        containerProgressbar.gone()
                    }
                }, {})
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
        when (view) {
            edtName -> {
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    edtName.apply {
                        requestFocus()
                        isCursorVisible = true
                    }
                    imgEmail.isSelected = true
                    line01.isSelected = true
                    imgPassword.isSelected = false
                    line02.isSelected = false
                }
            }
            edtPassword -> {
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    edtPassword.requestFocus()
                    imgEmail.isSelected = false
                    line01.isSelected = false
                    imgPassword.isSelected = true
                    line02.isSelected = true
                }
            }
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun initListener() {
        edtName.setOnTouchListener(this)
        edtPassword.setOnTouchListener(this)
        btnSubmit.setOnClickListener {
            val email = edtName.text.toString().trim()
            val password = edtPassword.text.toString().trim()

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
            containerProgressbar.visible()
            auth.signInWithEmailAndPassword(email, passWord)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        handePostUIDUser(email)
                    } else {
                        if (!viewModel.isInternetAvailable(ctx)) {
                            tvCheckPassword.text = ctx.getString(R.string.tv_check_connect_internet)
                        }
                    }
                }

        }
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
                                    address =  address,
                                    sex = sex,
                                    email = this.email,
                                    phone = phone,
                                    cmnd = cmnd)
                                replaceFragment(
                                    ConditionBorrowMoney.newInstance(user),
                                    true,
                                    AppConstant.TAG_NAME_LOGIN
                                )
                            }
                        }
                        usersRef.updateChildren(userUpdates as Map<String, Any>)
                    }
                }
            }, {})
    }
}
