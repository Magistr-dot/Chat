package com.example.chatvol4at

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_list.*

class UserList : AppCompatActivity() {
    lateinit var refDataBase:DatabaseReference
    lateinit var userChildEventListener: ChildEventListener
    lateinit var userAdapter: UserAdapter
    var userArray = arrayListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        attachUserDatabaseReferenceListener()
        buildRecycleView()

    }

    private fun attachUserDatabaseReferenceListener() {
         refDataBase = FirebaseDatabase.getInstance().reference.child("users")

            userChildEventListener = object :ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val user =  snapshot.getValue(User::class.java)
                    if (user != null) {
                        user.avatarMockUp = R.drawable.ic_baseline_person_outline_50
                        userArray.add(user)
                        userAdapter.notifyDataSetChanged()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        refDataBase.addChildEventListener(userChildEventListener)
        }


    private fun buildRecycleView() {
        userListRecycle.setHasFixedSize(true)
        var userLayoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userArray)

        userListRecycle.layoutManager = userLayoutManager
        userListRecycle.adapter = userAdapter
    }
}