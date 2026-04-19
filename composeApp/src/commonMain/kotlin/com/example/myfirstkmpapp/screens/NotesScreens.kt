package com.example.myfirstkmpapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmpapp.data.dummyNotes

@Composable
fun NotesListScreen(onNoteClick: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(dummyNotes) { note ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { onNoteClick(note.id) },
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(note.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(note.content, color = Color.Gray, maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Favorites\n(Kosong)", textAlign = TextAlign.Center, color = Color.Gray)
    }
}

@Composable
fun NoteDetailScreen(noteId: Int, onBackClick: () -> Unit, onEditClick: () -> Unit) {
    val note = dummyNotes.find { it.id == noteId }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, null) }
        Spacer(modifier = Modifier.height(16.dp))
        if (note != null) {
            Text(note.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(note.content, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onEditClick, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Edit, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit")
            }
        }
    }
}

@Composable
fun AddNoteScreen(onBackClick: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, null) }
        Text("Tambah Catatan", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Isi") }, modifier = Modifier.fillMaxWidth(), minLines = 5)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) { Text("Simpan") }
    }
}

@Composable
fun EditNoteScreen(noteId: Int, onBackClick: () -> Unit) {
    val note = dummyNotes.find { it.id == noteId }
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, null) }
        Text("Edit Catatan", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Isi") }, modifier = Modifier.fillMaxWidth(), minLines = 5)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) { Text("Simpan") }
    }
}