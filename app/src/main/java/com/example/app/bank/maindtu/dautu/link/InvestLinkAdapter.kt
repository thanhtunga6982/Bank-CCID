package com.example.app.bank.maindtu.dautu.link


import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.data.model.Bank
import com.example.app.bank.extention.inflate
import com.example.app.bank.extention.loadUrl
import kotlinx.android.synthetic.main.item_layout_invest.view.*

class InvestLinkAdapter(var listUser: MutableList<Bank>) : RecyclerView.Adapter<InvestLinkAdapter.LinkInvestVH>() {

    internal var userClickListeners: (bank: Bank) -> Unit = {}
    override fun onCreateViewHolder(container: ViewGroup, position: Int): InvestLinkAdapter.LinkInvestVH {
        return LinkInvestVH(container.inflate(R.layout.item_layout_invest, false))
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(viewHoder: LinkInvestVH, p1: Int) {
        return viewHoder.bindView(listUser[p1])
    }

    inner class LinkInvestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(bank: Bank) {
            itemView.tvNameBank.text = bank.name
            itemView.imgBank.loadUrl(bank.logo)
//                itemView.apply {
//                    setOnClickListener {
//                        userClickListeners(listUser[adapterPosition])
//                    }
//                    tvEmail.text = email
//                    tvName.text = name
//                    tvMoneyBorrow.text = moneyBorrow
//                    tvDebtPlan.text = debtpaymentplan
//                }
            itemView.viewGR.setOnClickListener {
                userClickListeners(listUser[adapterPosition])
            }
        }
    }
}