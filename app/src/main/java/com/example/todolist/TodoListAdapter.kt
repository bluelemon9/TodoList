package com.example.todolist

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class TodoListAdapter(realmResult: OrderedRealmCollection<Todo>)
    : RealmBaseAdapter<Todo>(realmResult) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vh: ViewHolder
        val view: View

        if(convertView == null) {   // convertView는 아이템이 작성되기 전에는 null임
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_todo, parent, false)  // 레이아웃 작성

            vh = ViewHolder(view)   // 뷰 홀더 객체 초기화
            view.tag = vh
        } else{
            view = convertView      // 이전에 작성했던 convertView 재사용
            vh = view.tag as ViewHolder
        }
        if(adapterData != null){
            val item = adapterData!![position]
            vh.textTextView.text = item.title
            vh.dateTextView.text = DateFormat.format("yyyy/MM/dd", item.date)
        }
        return view
    }

    override fun getItemId(position: Int): Long {
        if(adapterData != null){
            return adapterData!![position].id
        }
        return super.getItemId(position)
    }
}

class ViewHolder(view: View){
    val dateTextView: TextView = view.findViewById(R.id.text1)
    val textTextView: TextView = view.findViewById(R.id.text2)
}