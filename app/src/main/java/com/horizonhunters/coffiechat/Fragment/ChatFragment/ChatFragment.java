package com.horizonhunters.coffiechat.Fragment.ChatFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.horizonhunters.coffiechat.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize Firestore and user ID
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize RecyclerView
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatAdapter = new ChatAdapter(new ArrayList<>());
        chatRecyclerView.setAdapter(chatAdapter);

        loadChatList();


        // Initialize and set up views here
        return view;

    }

    private void loadChatList() {
        db.collection("chats")
                .whereArrayContains("participants", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Error loading chats: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Chat> chatList = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots) {
                        String lastMessage = doc.getString("lastMessage");
                        long timestamp = doc.getLong("timestamp");
                        List<String> participants = (List<String>) doc.get("participants");

                        String otherUserId = participants.stream()
                                .filter(id -> !id.equals(currentUserId))
                                .findFirst().orElse("");

                        chatList.add(new Chat(otherUserId, lastMessage, timestamp));
                    }

                    chatAdapter.updateChatList(chatList);
                });
    }
}