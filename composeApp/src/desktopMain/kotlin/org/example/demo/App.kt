package org.example.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import boglin_gui.composeapp.generated.resources.Res
import boglin_gui.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        // Sample data for contacts
        val contacts = remember {
            listOf(
                "Alice Smith",
                "Bob Johnson",
                "Carol Williams",
                "David Brown",
                "Eva Davis",
                "Frank Miller",
                "Grace Wilson",
                "Henry Moore",
                "Ivy Taylor",
                "Jack Anderson"
            )
        }

        // Track selected contact
        var selectedContact by remember { mutableStateOf(contacts.firstOrNull()) }

        Row(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
        ) {
            // Contacts list (left panel)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    "Contacts",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(contacts) { contact ->
                        Text(
                            text = contact,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .background(
                                    if (contact == selectedContact) 
                                        MaterialTheme.colorScheme.secondaryContainer
                                    else 
                                        MaterialTheme.colorScheme.background
                                )
                                .clickable { selectedContact = contact },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Spacer between panels
            Spacer(modifier = Modifier.width(8.dp))

            // Chat window (right panel)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                // Chat header
                Text(
                    selectedContact ?: "Select a contact",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp)
                )

                // Chat messages area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (selectedContact != null) {
                        Image(
                            painterResource(Res.drawable.compose_multiplatform),
                            contentDescription = "Compose Multiplatform logo",
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text("Chat with $selectedContact")
                        Text("This is where messages would appear", style = MaterialTheme.typography.bodySmall)
                    } else {
                        Text("Select a contact to start chatting")
                    }
                }

                // Message input area
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        "Type a message...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )

                    Button(
                        onClick = { /* Send message action */ },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    }
}
