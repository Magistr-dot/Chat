package com.example.chatvol4at

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_list.*
private lateinit var auth: FirebaseAuth
class UserList : AppCompatActivity() {
    lateinit var refDataBase: DatabaseReference
    lateinit var userChildEventListener: ChildEventListener
    lateinit var userAdapter: UserAdapter
    var userArray = arrayListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        auth = Firebase.auth

        var intent = intent
        var userName = intent.getStringExtra("userName")
        attachUserDatabaseReferenceListener()
        buildRecycleView()

    }

    private fun attachUserDatabaseReferenceListener() {
        refDataBase = FirebaseDatabase.getInstance().reference.child("users")

        userChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)

                if (user != null) {
                    if(user.id != auth.currentUser?.uid) {
                        user.avatarMockUp = R.drawable.ic_baseline_person_outline_50
                        userArray.add(user)
                        userAdapter.notifyDataSetChanged()
                    }


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInf = menuInflater
        menuInf.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, SignInActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)


    }


    private fun buildRecycleView() {
        userListRecycle.setHasFixedSize(true)
        userListRecycle.addItemDecoration(DividerItemDecoration(
            userListRecycle.context,
            LinearLayoutManager.VERTICAL
        ))
        val userLayoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userArray)


        userListRecycle.layoutManager = userLayoutManager
        userListRecycle.adapter = userAdapter

        userAdapter.setOnUserClickListener(object : UserAdapter.OnUserClickListener {
            override fun onUserClick(position: Int) {
                goToChat(position)
            }

        })
    }
    fun goToChat(position:Int) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("recipientUserId", userArray[position].id)
        intent.putExtra("recipientUserName", userArray[position].nickname)
        intent.putExtra("userName", userArray[position].nickname)
        startActivity(intent)
    }
}