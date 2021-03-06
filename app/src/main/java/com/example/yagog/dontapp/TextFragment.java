package com.example.yagog.dontapp;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tagsReference;
    private EditText tagEditText, contentEditText;
    private TextView currentTagTextView;
    private FloatingActionButton searchTagFab;
    private List<TextMode> textModeCollection;
    private boolean tagFound = false;

    private void setUpFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        tagsReference = firebaseDatabase.getReference("tags");
    }


    public TextFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_text, container, false);

        setUpFirebase();

        tagEditText = v.findViewById(R.id.tagEditText);
        contentEditText = v.findViewById(R.id.contentEditText);
        currentTagTextView = v.findViewById(R.id.currentTagTextView);
        searchTagFab = (FloatingActionButton) v.findViewById(R.id.searchTagFab);
        final TextMode textMode = new TextMode();
        textModeCollection = new LinkedList<TextMode>();
        tagsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textModeCollection.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    TextMode textMode = child.getValue(TextMode.class);
                    textMode.setKey(child.getKey());
                    textModeCollection.add(textMode);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(TextFragment.this, getString(R.string.firebase_error), Toast.LENGTH_SHORT).show();
            }
        });
        //Botão que realiza a busca da Tag
        searchTagFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux = "";
                textMode.setTag(tagEditText.getEditableText().toString());
                currentTagTextView.setText(textMode.getTag());
                tagEditText.setText("");
                //Buscando a tag no Firebase (Se existir)
                for(TextMode tagOnSearch : textModeCollection){
                    if(textMode.getTag().equals(tagOnSearch.getTag().toString())){
                        tagFound = true;
                        aux = tagOnSearch.getTag();
                        String key = tagOnSearch.getKey();
                        textMode.setKey(key);
                        textMode.setContent(tagOnSearch.getContent());
                    }
                }
                //Caso não exista, criar o registro
                if (!tagFound || !textMode.getTag().equals(aux)){
                    tagFound = false;
                    String key = tagsReference.push().getKey();
                    textMode.setKey(key);
                    textMode.setContent(contentEditText.getEditableText().toString());
                    tagsReference.child(textMode.getKey()).setValue(textMode);
                    contentEditText.setText("");
                }
                contentEditText.setText(textMode.getContent());
            }
        });

        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //Salvando conteudo no Firebase
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textMode.setContent(s.toString());
                tagsReference.child(textMode.getKey()).setValue(textMode);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return v;
    }
}