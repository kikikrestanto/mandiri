package com.example.mandiri.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mandiri.Activites.AddPostMaintenanceAct;
import com.example.mandiri.Activites.LoginAct;
import com.example.mandiri.Adapters.AdapterAddPostMaintenance;
import com.example.mandiri.Models.ModelAddMaintenance;
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

public class DateFragment extends Fragment {


    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String email,uid;
    List<ModelAddMaintenance> addPost;
    AdapterAddPostMaintenance adapterAddPostMaintenance;
    RecyclerView recyclerView;

    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date,container,false);

        //init
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //recycler view and its properties
        recyclerView = view.findViewById(R.id.dateRecycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        //show newest post first, for this load from last
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        //set layout to recycler view
        recyclerView.setLayoutManager(linearLayoutManager);

        addPost = new ArrayList<>();

        checkUserStatus();

        loadPost();

        return  view;

    }

    private void loadPost() {
        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager); */

        //init post list
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostMaintenance");
        //get all data from ref
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addPost.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelAddMaintenance modelAddMaintenance = ds.getValue(ModelAddMaintenance.class);

                    addPost.add(modelAddMaintenance);

                    //adapter
                    adapterAddPostMaintenance = new AdapterAddPostMaintenance(getActivity(),addPost);

                    //set adapter to recycler
                    recyclerView.setAdapter(adapterAddPostMaintenance);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_add_post).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        if (id == R.id.action_add_post) {
            startActivity(new Intent(getActivity(), AddPostMaintenanceAct.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
