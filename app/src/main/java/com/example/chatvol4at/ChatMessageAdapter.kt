package com.example.chatvol4at

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.message.view.*
import kotlinx.android.synthetic.main.my_message_item.view.*


class ChatMessageAdapter(context: Activity, resource: Int, list: ArrayList<ChatMessage>) :
    ArrayAdapter<ChatMessage>(context, resource, list) {
    var resource: Int
    var list: ArrayList<ChatMessage>
    var vi: LayoutInflater
    var activity: Activity

    init {
        this.list = list
        this.activity = context
    }


    init {
        this.resource = resource
        this.list = list
        this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
 /*       var  viewHolder= convertView?.let { ViewHolder(it) }
        if (viewHolder != null) {
            var layoutInflater =
                activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater


                var conView = convertView

            var message = getItem(position)
            var layoutRes = 0
            var viewType = getItemViewType(position)

            layoutRes = if (viewType == 0) {
                R.layout.my_message_item
            } else {
                R.layout.your_message_item
            }
            if (conView != null) {
                if (convertView != null) {
                    viewHolder = convertView.tag as ViewHolder
                }
            } else {
                conView = layoutInflater.inflate(layoutRes, parent, false)
                viewHolder = ViewHolder(conView)
                convertView?.tag = viewHolder
            }
            var isText = true
            if (message != null) {
                var isText = message.imageUrl == null
            }

            if (isText) {
                viewHolder.photo.visibility = View.GONE
                viewHolder.text.visibility = View.VISIBLE
                if (message != null) {
                    viewHolder.text.text = message.text
                }

            } else {
                viewHolder.photo.visibility = View.VISIBLE
                viewHolder.text.visibility = View.GONE
                if (message != null) {
                    Glide.with(viewHolder.photo.context).load(message.imageUrl)
                        .into(viewHolder.photo)
                }
            }

        }
        return convertView ?: vi.inflate(R.layout.message, parent, false)*/
        var layoutRes = 0
        var viewType = getItemViewType(position)
        layoutRes = if (viewType == 0) {
            R.layout.your_message_item
        } else {
            R.layout.my_message_item
        }

        val retView: View = vi.inflate(layoutRes, parent, false)
        val currentMessage = getItem(position)
        if (currentMessage != null) {
            if (currentMessage.imageUrl == null) {
                retView.photoImage.visibility = View.GONE
                retView.bubbleText.visibility = View.VISIBLE
                retView.bubbleText.text = currentMessage.text
            } else {
                retView.photoImage.visibility = View.VISIBLE
                retView.bubbleText.visibility = View.GONE
                Glide.with(retView.photoImage.context).load(currentMessage.imageUrl).into(retView.photoImage)
            }
//            retView.name.text = currentMessage.name


        }
        return retView
    }

    override fun getItemViewType(position: Int): Int {

        var flag = 0
        val mMessage = list[position]
        flag = if (mMessage.isMine) {
            0

        } else {
            1
        }
        return flag
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    class ViewHolder(view: View) {
        var photo = view.photoImage
        var text = view.bubbleText
    }
}