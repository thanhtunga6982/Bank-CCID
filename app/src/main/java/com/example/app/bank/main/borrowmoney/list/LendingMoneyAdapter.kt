package com.example.app.bank.main.borrowmoney.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.inflate
import kotlinx.android.synthetic.main.item_lending_money.view.*

class LendingMoneyAdapter(val listUser: MutableList<User>) :
    RecyclerView.Adapter<LendingMoneyAdapter.LendingMoneyViewHolder>() {

    internal var userClickListeners: (user: User) -> Unit = {}
    override fun onCreateViewHolder(container: ViewGroup, p1: Int): LendingMoneyAdapter.LendingMoneyViewHolder {
        return LendingMoneyViewHolder(container.inflate(R.layout.item_lending_money, false))
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(viewHolder: LendingMoneyAdapter.LendingMoneyViewHolder, position: Int) {
        return viewHolder.bindView(listUser[position])
    }

    inner class LendingMoneyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(user: User) {
            user.run {
                itemView.apply {
                    setOnClickListener {
                        userClickListeners(listUser[adapterPosition])
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