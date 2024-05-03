package com.ebraratabay.mobvenhomework1

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ebraratabay.mobvenhomework1.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActiviyScreen()
            refreshData()

        }

    }
}
var usersList: MutableList<User> by mutableStateOf(mutableListOf())

@Composable
fun MainActiviyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        textField()
        UserList()

    }
}


@Composable
fun textField() {
    var filledName by remember { mutableStateOf("") }
    var filledSurname by remember { mutableStateOf("") }
    var filledPhoneNumber by remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxWidth(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = filledName,
            onValueChange = { filledName = it },
            label = { Text("Name") },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "name"
                )
            })
        TextField(
            value = filledSurname,
            onValueChange = { filledSurname = it },
            label = { Text("Surname") },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "surname"
                )
            })
        TextField(
            value = filledPhoneNumber,
            onValueChange = { filledPhoneNumber = it },
            label = { Text("Phone Number") },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "age"
                )
            }
        )


        Button(onClick = {
            var user= User(filledName,filledSurname,filledPhoneNumber)
            sendToDB(user) }) {
            Text(text = "Save")
        }
    }


}
val db= Firebase.firestore
fun sendToDB(user: User){

    db.collection("Users").add(user)
        .addOnSuccessListener { documentReference ->
            Log.d("SuccesFirebase", "DocumentSnapshot added with ID: ${documentReference.id}")
            refreshData()
        }
        .addOnFailureListener { e ->
            Log.w("FailFirebase", "Error adding document", e)
        }

}
fun refreshData(){
    db.collection("Users").get().addOnSuccessListener { result ->
        for (user in result) {
            val username = user["name"].toString()
            val usersurname = user["surname"].toString()
            val phonenumber=user["phoneNumber"].toString()
            val user= User(username, usersurname, phonenumber)
            println(user.name)
            println(user.surname)
            usersList.add(user)
        }
    }
        .addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }
}

@Composable
fun UserList(){
    refreshData()
    LazyColumn( ){
        items(items= usersList) { user ->
            ListItem(user = user)
        }
    }
}

@Composable
fun ListItem(user:User){
    Row(modifier= Modifier.fillMaxWidth().padding(top = 8.dp).border(1.dp, Color.Black),
        horizontalArrangement = Arrangement.SpaceEvenly){
        Text(text=user.name, Modifier.padding(vertical = 4.dp))
        Text(text=user.surname, Modifier.padding(vertical = 4.dp))
        Text(text=user.phoneNumber, Modifier.padding(vertical = 4.dp))
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainActiviyScreen()
}