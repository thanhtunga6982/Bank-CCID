package com.example.app.bank.maindtu.dautu.sangiaodich


import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.inflate
import kotlinx.android.synthetic.main.item_lending_money.view.*

class SGDAdapter(var listUser: MutableList<User>) : RecyclerView.Adapter<SGDAdapter.SGDViewHolder>() {

    internal var userClickListeners: (user: User) -> Unit = {}
    override fun onCreateViewHolder(container: ViewGroup, position: Int): SGDViewHolder {
        return SGDViewHolder(container.inflate(R.layout.item_lending_money, false))
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(viewHoder: SGDViewHolder, p1: Int) {
        return viewHoder.bindView(listUser[p1])
    }

    inner class SGDViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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