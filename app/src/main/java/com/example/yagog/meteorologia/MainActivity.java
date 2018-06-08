package com.example.yagog.meteorologia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference cidadesReference;
    private EditText cidadeEditText;
    private TextView cidadeTextView;

    private void configuraDatabase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        cidadesReference = firebaseDatabase.getReference("cidades");
    }

    @Override
    protected void onStart() {
        super.onStart();
        cidadesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cidades.clear();
                for (DataSnapshot filho : dataSnapshot.getChildren()){
                    Cidade cidade = filho.getValue(Cidade.class);
                    cidade.setChave(filho.getKey());
                    cidades.add(cidade);
                }
                cidadesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, getString(R.string.erro_firebase), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ListView cidadesListView;
    private ArrayAdapter<Cidade> cidadesAdapter;
    private List<Cidade> cidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cidadeTextView = findViewById(R.id.cidadeTextView);
        cidadeEditText = findViewById(R.id.cidadeEditText);
        cidadesListView = findViewById(R.id.cidadesListView);
        configuraDatabase();
        cidades = new Stack<Cidade>();
        //cidadesAdapter = new ArrayAdapter<Cidade>(this,android.R.layout.simple_list_item_1, cidades);
        cidadesAdapter = new ArrayAdapter<Cidade>(this,-1, cidades){
        class ViewHolder{
            TextView cidadeTextView;
        }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
                ViewHolder viewHolder;
                TextView cidadeTextView;

                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_cidades, parent, false);
                    viewHolder = new ViewHolder();
                    cidadeTextView = convertView.findViewById(R.id.cidadeTextView);
                    viewHolder.cidadeTextView = cidadeTextView;
                    convertView.setTag(viewHolder);
                }
                else{
                    viewHolder = (ViewHolder) convertView.getTag();
                    cidadeTextView = viewHolder.cidadeTextView;
                }

                Cidade c = cidades.get(position);

                cidadeTextView.setText(c.getNome());

                return convertView;
            }
        };
        cidadesListView.setAdapter(cidadesAdapter);

        cidadesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(MainActivity.this);
                dBuilder.setMessage(getString(R.string.confirmar_remocao));
                dBuilder.setPositiveButton(getString(R.string.exclusao_confirmada), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cidade vaiSerRemovida = cidades.get(position);
                        cidadesReference.child(vaiSerRemovida.getChave()).removeValue();
                        Toast.makeText(MainActivity.this, getString(R.string.cidade_removida), Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(getString(R.string.exclusao_negada), null).show();
                return true;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cidadeNome = cidadeEditText.getEditableText().toString();
                String chave = cidadesReference.push().getKey();
                Cidade cidade = new Cidade (chave, cidadeNome);
                cidadesReference.child(chave).setValue(cidade);
                Toast.makeText(MainActivity.this, getString(R.string.cidade_adicionada), Toast.LENGTH_SHORT).show();
            }
        });
        configuraDatabase();

        cidadesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cidade cidade = (Cidade) cidadesListView.getItemAtPosition(position);
                Intent intent = new Intent (MainActivity.this, PrevisaoActivity.class);
                intent.putExtra("CIDADE", cidade.getNome());
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
