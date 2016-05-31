package com.example.ash.reduxlmplementation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Person> persons;
    private Store<Action<ActionType, Person>, List<Person>> store;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.recyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        initializeData();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.persons);
        this.store = new Store<>(this.persons);
        this.store.addReducer(this.reducer);
        this.store.subscribeView(this.observer);

        adapter.setListener(recyclerViewItemClickListener);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onAddUserClickListener);
    }

    private void initializeData() {
        List<Person> creationList = new ArrayList<>();
        creationList.add(new Person("Emma Watson", "23 years old", R.drawable.emma_watson));
        creationList.add(new Person("Lavery Maiss", "25 years old", R.drawable.holly));
        creationList.add(new Person("Lillie Watts", "35 years old", R.drawable.vurst));
        persons = Collections.unmodifiableList(creationList);
    }

    private Reducer<List<Person>, Action<ActionType, Person>> reducer = new Reducer<List<Person>, Action<ActionType, Person>>() {
        @Override
        public List<Person> reduce(List<Person> oldState, Action<ActionType, Person> action) {
            List<Person> result = new ArrayList<Person>(oldState);
            switch (action.type) {
                case ADD_PERSON:
                    result.add(action.value);
                    break;
                case DELETE_PERSON:
                    result.remove(action.value);
                    break;
            }
            return result;
        }
    };

    private Observer observer = new Observer() {
        @Override
        public void stateHasBeenChanged() {
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(store.getState());
            adapter.setListener(recyclerViewItemClickListener);
            recyclerView.setAdapter(adapter);
        }
    };

    private RecyclerViewItemClickListener recyclerViewItemClickListener = new RecyclerViewItemClickListener() {
        @Override
        public void onItemClick(Person person) {
            store.dispatch(new Action<>(ActionType.DELETE_PERSON, person));
        }
    };

    private View.OnClickListener onAddUserClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showAddItemDialog();
        }
    };

    private void showAddItemDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText nameBox = new EditText(this);
        nameBox.setHint("Name:");
        layout.addView(nameBox);

        final EditText ageBox = new EditText(this);
        ageBox.setHint("Age:");
        layout.addView(ageBox);

        new AlertDialog.Builder(this)
                .setTitle("Add Item:")
                .setView(layout)
                .setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        store.dispatch(new Action<>(ActionType.ADD_PERSON, new Person(nameBox.getText().toString(),
                                ageBox.getText().toString(), R.drawable.unknown)));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
