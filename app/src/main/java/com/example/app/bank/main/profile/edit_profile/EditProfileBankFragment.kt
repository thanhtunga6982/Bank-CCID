package com.example.app.bank.main.profile.edit_profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.loadUrl
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.layout_header_profile.*


class EditProfileBankFragment : BaseFragment() {

    companion object {

        private const val KEY_PROFILE = "key_profile"
        fun newInstance(user: User) = EditProfileBankFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_PROFILE, user)
            }
        }
    }

    private var user = User()

    private var tvMale = ""
    private var tvFeMale = ""
    private lateinit var viewModel: EditProfileBankViewModel
    private var firebase = FirebaseDatabase.getInstance().reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = it.getParcelable(KEY_PROFILE)
        }
        viewModel = EditProfileBankViewModel(LocalRepository())
        return inflater.inflate(com.example.app.bank.R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClick()
    }

    private fun initView() {
        with(user) {
            edtNameProfile.setText(name)
            edtAddressProfile.setText(address)
            tvValueTotalAsset.text = totalasset
            imgCircleAvatarEdit.loadUrl(avatar)
            edtValueEmailProfileEdit.setText(email)
            edtValueCmndProfile.setText(cmnd)
            edtValueAssetTaxProfile.setText(assettax)
            tvValuePhone.text = phone
        }

        // set sex default checkbox when open
        if (user.sex == EnumProfileEdit.MALE.type) {
            rbnMale.isChecked = true
        } else {
            rbnFemale.isChecked = true
        }

        rgpSex.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbnMale -> {
                    user.sex = EnumProfileEdit.MALE.type
                }
                R.id.rbnFemale -> {
                    user.sex = EnumProfileEdit.FEMALE.type
                }
            }
        }
    }

    private fun initClick() {
        imgCloseEdit.setOnClickListener {
            parentFragment?.let {
                if (it is BaseFragmentContainer) {
                    popBackStack()
                }
            }
        }

        btnAgree.setOnClickListener {
            getValueInfoProfile()
            handleUserUpdateProfile()
        }
    }

    private fun getValueInfoProfile() {
        with(user) {
            name = edtNameProfile.text.toString()
            address = edtAddressProfile.text.toString()
            email = edtValueEmailProfileEdit.text.toString()
            cmnd = edtValueCmndProfile.text.toString()
            assettax = edtValueAssetTaxProfile.text.toString()
        }
    }

    @SuppressLint("CheckResult")
    private fun handleUserUpdateProfile() {
        viewModel.getUserList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ it ->
                for (i in 0 until it.size) {
                    if (it[i].email == user.email) {
                        val userUpdates = HashMap<String, String>()
                        val usersRef = firebase.child("listUser")
                        //get key object on firebase
                        it[i].key.let { key ->
                            userUpdates["$key/sex"] = user.sex
                            userUpdates["$key/name"] = user.name
                            userUpdates["$key/address"] = user.address
                            userUpdates["$key/email"] = user.email
                            userUpdates["$key/cmnd"] = user.cmnd
                            userUpdates["$key/assettax"] = user.assettax
                        }
                        usersRef.updateChildren(userUpdates as Map<String, Any>)
                    }
                    imgCloseEdit.performClick()
                }
            }, {})
    }

    override fun onBindViewModel() {
        //TODO
    }
}
