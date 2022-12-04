package com.blaze.makesnotes;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menubtn;
    noteAdapter noteadapter;

   // Switch aSwitch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  getSupportActionBar().hide();
      //  aSwitch.findViewById(R.id.switcher);
      //  aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         /*   @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        }); */

        addNoteBtn=findViewById(R.id.add_note_btn);
        recyclerView= findViewById(R.id.recyler_view);
        menubtn=findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this,NoteDetailsActivity.class)));
        menubtn.setOnClickListener((v)->showMenu());
        setupRecyclerView();

    }

    void showMenu(){
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,menubtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class) );
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    void setupRecyclerView(){
        Query query=Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options=new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteadapter = new noteAdapter(options,this);
        recyclerView.setAdapter(noteadapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteadapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteadapter.notifyDataSetChanged();
    }
}