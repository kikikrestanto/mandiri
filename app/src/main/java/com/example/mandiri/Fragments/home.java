package com.example.mandiri.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mandiri.Activites.AddPostAct;
import com.example.mandiri.Activites.LoginAct;
import com.example.mandiri.Activites.RegisterAct;
import com.example.mandiri.Adapters.AdapterPosts;
import com.example.mandiri.Models.ModelPost;
import com.example.mandiri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelPost> postList;

    AdapterPosts adapterPosts;


    String mUID, email,uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        //recycler view and its properties
        recyclerView = view.findViewById(R.id.postRecycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //show newest post first, for this load from last
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        //set layout to recycleview
        recyclerView.setLayoutManager(linearLayoutManager);

        //init post list
        postList = new ArrayList<>();

        loadPoasts();


        return view;
    }

    private void loadPoasts() {
        //path of all post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //get all data from ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);
                    //adapter
                    adapterPosts = new AdapterPosts(getActivity(),postList);
                    //set adapter to recycler
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case error
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private  void  searchPosts(final String searchQuery){
        //path of all post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //get all data from ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    if (modelPost.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())){
                        postList.add(modelPost);
                    }
                    //adapter
                    adapterPosts = new AdapterPosts(getActivity(),postList);
                    //set adapter to recycler
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case error
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus() {
        // get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // user signed stay here
            // set email of logged user
            //mProfileTv.setText(user.getEmail());

            email = user.getEmail();
            uid = user.getUid();

        } else {
            // user not sign in, go to Register
            startActivity(new Intent(getActivity(), LoginAct.class));
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);//to show option menu in fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // inflating
        inflater.inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_logout).setVisible(true);



        //searchview to search posts by post title/ description
        MenuItem item = menu.findItem(R.id.action_search).setVisible(true);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when users press search button
                if (!TextUtils.isEmpty(s)){
                    searchPosts(s);
                }else{
                    loadPoasts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called as and when user press any letter
                if (!TextUtils.isEmpty(s)){
                    searchPosts(s);
                }else{
                    loadPoasts();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    // handle menu item clicks


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        if (id == R.id.action_add_post) {
            startActivity(new Intent(getActivity(), AddPostAct.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
