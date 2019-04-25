package com.example.app.bank.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.loadUrl
import com.example.app.bank.main.profile.edit_profile.EditProfileBankFragment
import com.example.app.bank.main.profile.edit_profile.EnumProfile
import com.example.app.bank.main.profile.edit_profile.EnumProfileEdit
import kotlinx.android.synthetic.main.fragment_profile_bank.*
import kotlinx.android.synthetic.main.layout_header_profile.*

class ProfileBankFragment() : BaseFragment() {

    companion object {

        private const val KEY_PROFILE = "key_profile"
        fun newInstance(user: User) = ProfileBankFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_PROFILE, user)
            }
        }
    }

    private var user = User()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = it.getParcelable(KEY_PROFILE)
        }
        return inflater.inflate(R.layout.fragment_profile_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClick()
    }

    private fun initView() {
        with(user) {
            tvNameProfile.text = name
            imgCircleAvatar.loadUrl(avatar)
            tvAddressProfile.text = address
            tvValueTotalAsset.text = totalasset
            tvValuePhone.text = phone
            tvValueEmailProfile.text = email
            tvValueCmndProfile.text = cmnd
            tvValueAssetTaxProfile.text = assettax
            if (sex == EnumProfileEdit.MALE.type) {
                tvValueSexProfile.text = EnumProfile.MALE.type
            } else {
                tvValueSexProfile.text = EnumProfile.FEMALE.type
            }
        }
    }

    private fun initClick() {
        imgClose.setOnClickListener {
            parentFragment?.let {
                if (it is BaseFragmentContainer) {
                    popBackStack()
                }
            }
        }
        imgEditClose.setOnClickListener {
            replaceFragment(EditProfileBankFragment.newInstance(user), true)
        }
    }

    override fun onBindViewModel() {
        //TODO
    }

}
