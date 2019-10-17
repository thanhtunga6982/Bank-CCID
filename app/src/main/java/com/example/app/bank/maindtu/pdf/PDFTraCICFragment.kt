package com.example.app.bank.maindtu.pdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.model.PdfBus
import com.example.app.bank.data.source.RxBus
import kotlinx.android.synthetic.main.fragment_pdf_cic.*
import kotlinx.android.synthetic.main.layout_header_app.*


class PDFTraCICFragment() : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pdf_cic, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pdfView.fromAsset("sample.pdf").load()
        initListener()

    }

    private fun initListener() {
        imgClose.setOnClickListener {
            popBackStack()
            RxBus.publish(PdfBus(true))
        }
    }


    override fun onBindViewModel() {
        //TODO
    }
}