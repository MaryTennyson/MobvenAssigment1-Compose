package com.ebraratabay.mobvenhomework1.views


import android.content.ContentValues
import android.util.Log
import com.ebraratabay.mobvenhomework1.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainViewModel {
    val db = Firebase.firestore

    //  var userLists ={ mutableStateListOf<User>()}
    fun sendToDB(user: User) {
        db.collection("Users").add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("SuccesFirebase", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FailFirebase", "Error adding document", e)
            }

    }

    fun refreshData(updateList: (ArrayList<User>) -> Unit) {
        var usersList = ArrayList<User>()
        db.collection("Users").get().addOnSuccessListener { result ->
            for (user in result) {
                val username = user["name"].toString()
                val usersurname = user["surname"].toString()
                val phonenumber = user["phoneNumber"].toString()
                val user = User(username, usersurname, phonenumber)
                println(user.name)
                println(user.surname)
                usersList.add(user)
            }
            updateList(usersList)
        }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}