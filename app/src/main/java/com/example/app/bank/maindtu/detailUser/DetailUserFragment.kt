package com.example.app.bank.maindtu.detailUser

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.data.model.PdfBus
import com.example.app.bank.data.model.User
import com.example.app.bank.data.source.RxBus
import com.example.app.bank.dialog.BorrowMoneyErrorDialog
import com.example.app.bank.dialog.BorrowMoneySuccessDialog
import com.example.app.bank.dialog.DialogSGDSussess
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.visible
import com.example.app.bank.maindtu.pdf.PDFTraCICFragment
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail_user.*
import kotlinx.android.synthetic.main.layout_detail_user.*
import kotlinx.android.synthetic.main.layout_header_app.*

class DetailUserFragment() : BaseFragment(), View.OnClickListener {


    companion object {
        private const val USER_DETAIL = "user_detail"
        private const val USER_DETAIL_CHECK = "user_detail_check"
        fun newInstance(user: User, isCheckInvest: Boolean) = DetailUserFragment().apply {
            arguments = Bundle().apply {
                putParcelable(USER_DETAIL, user)
                putBoolean(USER_DETAIL_CHECK, isCheckInvest)
            }
        }
    }

    private var firebase = FirebaseDatabase.getInstance().reference

    private var isCheckInvest = false;
    private lateinit var dialogDetail: BorrowMoneySuccessDialog
    private lateinit var dialogDetailError: BorrowMoneyErrorDialog
    private lateinit var dialogSGDsussess: DialogSGDSussess
    private lateinit var viewModel: DetailUserViewModel
    private var user = User()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = it.getParcelable(USER_DETAIL)
            isCheckInvest = it.getBoolean(USER_DETAIL_CHECK)
        }
        dialogDetail = BorrowMoneySuccessDialog()
        dialogDetailError = BorrowMoneyErrorDialog()
        dialogSGDsussess = DialogSGDSussess();
        viewModel = DetailUserViewModel()
        return inflater.inflate(R.layout.fragment_detail_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClick()
    }

    @SuppressLint("CheckResult")
    private fun initClick() {
        if (isCheckInvest) {
            btnCIC.visible()
            btnApply.visible()
        } else {
            btnCIC.gone()
            btnApply.gone()
        }
        btnApply.isEnabled = false
        btnCIC.setOnClickListener(this)
        btnApply.setOnClickListener(this)
        imgClose.setOnClickListener(this)
        RxBus.listen(PdfBus::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isRead) {
                    btnApply.isEnabled = true
                }
            }, {})
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnCIC -> {
                replaceFragment(PDFTraCICFragment(), true)
            }
            btnApply -> {
                val moneyBorrow = user.moneyBorrow.replace(",", "").toInt()
                val totalAsset = user.totalasset.replace(",", "").toInt()
                handleClickCIC(totalAsset, moneyBorrow)
            }
            imgClose -> {
                parentFragment?.let {
                    if (it is BaseFragmentContainer) {
                        popBackStack()
                    }
                }
            }
        }
    }

    private fun initView() {
        with(user) {
            tvNameDetail.text = name
            tvEmailDetail.text = email
            tvMoneyDetail.text = moneyBorrow
            tvAssetTaxDetail.text = assettax
            tvDebtpaymentplanDetail.text = debtpaymentplan
            tvTotalAssetDetail.text = totalasset
            tvAddressDetail.text = address
        }
        tvTitleHeader.text = "Chi tiết người cần vay tiền"
    }


    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleClickCIC(totalAsset: Int, moneyBorrow: Int) {
        addDisposables(viewModel.timerDelayProgressbar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it) {
                    progressBarDetail.gone()
                    if (totalAsset >= moneyBorrow) {
                        handleShowBorrowMoneySuccessDialog(dialogDetail)
                    } else {
                        dialogDetailError.show(
                            childFragmentManager,
                            BorrowMoneyErrorDialog::class.java.simpleName
                        )
                        viewModel.autoDismissDialog()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                dialogDetailError.dismiss()
                                user.isCheckMoney = true
                                popBackStackTagName("apply_invest")
                            }, {})
                    }
                } else {
                    progressBarDetail.visible()
                }
            })
    }


    @SuppressLint("CheckResult")
    private fun handleShowBorrowMoneySuccessDialog(borrowMoneyErrorDialog: BorrowMoneySuccessDialog) {
        borrowMoneyErrorDialog.show(childFragmentManager, BorrowMoneySuccessDialog::class.java.simpleName)
        addDisposables(viewModel.autoDismissDialog()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                borrowMoneyErrorDialog.dismiss()
            }
        )
        showdialogSuccess()
    }

    @SuppressLint("CheckResult")
    private fun showdialogSuccess() {
        viewModel.getTimer()
        addDisposables(viewModel.timerDelayProgressbar2
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                progressBarDetail.gone()
                if (it) {
                    dialogSGDsussess.show(childFragmentManager, DialogSGDSussess::class.java.simpleName)
                    addDisposables(viewModel.autoDismissDialog2()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val moneyBorrow = user.moneyBorrow.replace(",", "")
                            parentFragment.let {
                                if (it is BaseFragmentContainer) {
                                    it.setMoney(moneyBorrow)
                                    popBackStackTagName("apply_invest")
                                }
                            }

                            // set User to listHistory
                            handleUpdateHistory()

                            //remove User
                            firebase.child("listLending").child(user.key).removeValue()
                        })
                } else {
                    progressBarDetail.visible()
                }
            })
    }

    private fun handleUpdateHistory() {
        val id = firebase.push().key
        firebase.child("listHistory").child(id.toString()).setValue(user)

    }

    override fun onBindViewModel() {

    }
}
