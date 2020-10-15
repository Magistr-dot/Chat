package com.example.chatvol4at

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.message.view.*


class ChatMessageAdapter(context: Context, resource: Int, list: ArrayList<ChatMessage>) :
    ArrayAdapter<ChatMessage>(context, resource, list) {
    var resource: Int
    var list: ArrayList<ChatMessage>
    var vi: LayoutInflater

    init {
        this.resource = resource
        this.list = list
        this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var retView:View = vi.inflate(R.layout.message, parent, false)

        var currentMessage = getItem(position)
        if (currentMessage != null) {
            if (currentMessage.imageUrl == null) {
                retView.photo.visibility = View.GONE
                retView.textView.visibility = View.VISIBLE
                retView.textView.text = currentMessage.text
            } else {
                retView.photo.visibility = View.VISIBLE
                retView.textView.visibility = View.GONE
                Glide.with(retView.photo.context).load(currentMessage.imageUrl).into(retView.photo)
            }
            retView.name.text = currentMessage.name

        }
        return retView
    }
}