package com.example.app.bank.main.detailUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.data.model.User
import com.example.app.bank.dialog.BorrowMoneyErrorDialog
import com.example.app.bank.dialog.BorrowMoneySuccessDialog
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail_user.*
import kotlinx.android.synthetic.main.layout_detail_user.*
import kotlinx.android.synthetic.main.layout_header_app.*

class DetailUserFragment() : BaseFragment() {
    companion object {
        private const val USER_DETAIL = "user_detail"
        fun newInstance(user: User) = DetailUserFragment().apply {
            arguments = Bundle().apply {
                putParcelable(USER_DETAIL, user)
            }
        }
    }

    private lateinit var dialogDetail: BorrowMoneySuccessDialog
    private lateinit var dialogDetailError: BorrowMoneyErrorDialog
    private lateinit var viewModel: DetailUserViewModel
    private var user = User()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = it.getParcelable(USER_DETAIL)
        }
        dialogDetail = BorrowMoneySuccessDialog()
        dialogDetailError = BorrowMoneyErrorDialog()
        viewModel = DetailUserViewModel()
        return inflater.inflate(R.layout.fragment_detail_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClick()
    }

    private fun initClick() {
        btnCIC.setOnClickListener {
            val moneyBorrow = user.moneyBorrow.replace(",", "").toInt()
            val totalAsset = user.totalasset.replace(",", "").toInt()
            handleClickCIC(totalAsset, moneyBorrow)
        }
        imgClose.setOnClickListener {
            parentFragment?.let {
                if (it is BaseFragmentContainer) {
                    popBackStack()
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
        }
        tvTitleHeader.text =  "Chi tiết người cần vay tiền"
    }

    private fun handleClickCIC(totalAsset: Int, moneyBorrow: Int) {
        addDisposables(viewModel.timerDelayProgressbar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it) {
                    progressBarDetail.gone()
                    if (totalAsset >= moneyBorrow) {
                        dialogDetail.show(childFragmentManager, BorrowMoneySuccessDialog::class.java.simpleName)
                        viewModel.autoDismissDialog()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                dialogDetail.dismiss()
                            }, {})
                    } else {
                        dialogDetailError.show(
                            childFragmentManager,
                            BorrowMoneySuccessDialog::class.java.simpleName
                        )
                        viewModel.autoDismissDialog()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                dialogDetailError.dismiss()
                            }, {})
                    }
                } else {
                    progressBarDetail.visible()
                }
            })
    }

    override fun onBindViewModel() {

    }
}