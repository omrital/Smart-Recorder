package com.omrital.smartrecorder.core.activities

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.omrital.smartrecorder.R
import kotlinx.android.synthetic.main.record_list_item.view.*

class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTextView: TextView = itemView.name_text_view
}

class RecordListAdapter(private var context: Context, private var model: RecordViewModel, private var records: List<Record>): RecyclerView.Adapter<RecordViewHolder>() {

    fun setRecords(records: List<Record>) {
        this.records = records
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(LayoutInflater.from(context).inflate(R.layout.record_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val recordName = records[position].name
        holder.nameTextView.text = recordName
        holder.itemView.setOnClickListener {
            model.playRecord(recordName)
        }
    }

    override fun getItemCount(): Int {
        return records.size
    }
}