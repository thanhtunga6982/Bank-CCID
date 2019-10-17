package com.example.app.bank.maindtu.history


import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.inflate
import com.example.app.bank.maindtu.home.HomeAdapter
import kotlinx.android.synthetic.main.item_lending_money.view.*

class HistoryAdapter(var listHistory: MutableList<User>) : RecyclerView.Adapter<HistoryAdapter.HistoryVH>() {

    internal var userClickListeners: (user: User) -> Unit = {}
    override fun onCreateViewHolder(container: ViewGroup, position: Int): HistoryVH {
        return HistoryVH(container.inflate(R.layout.item_lending_money, false))
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    override fun onBindViewHolder(viewHoder: HistoryVH, p1: Int) {
        return viewHoder.bindView(listHistory[p1])
    }

    inner class HistoryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: User) {
            user.run {
                itemView.apply {
                    setOnClickListener {
                        userClickListeners(listHistory[adapterPosition])
                    }
                    tvEmail.text = email
                    tvName.text = name
                    tvMoneyBorrow.text = moneyBorrow
                    tvDebtPlan.text = debtpaymentplan
                }
            }
        }
    }
}