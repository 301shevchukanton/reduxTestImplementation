package com.example.ash.reduxlmplementation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;
import trikita.anvil.recyclerview.Recycler;

import static trikita.anvil.BaseDSL.dip;
import static trikita.anvil.BaseDSL.padding;
import static trikita.anvil.BaseDSL.size;
import static trikita.anvil.BaseDSL.toRightOf;
import static trikita.anvil.DSL.bottom;
import static trikita.anvil.DSL.id;
import static trikita.anvil.DSL.imageButton;
import static trikita.anvil.DSL.imageResource;
import static trikita.anvil.DSL.imageView;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.onClick;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.scaleType;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textView;

public class ReactiveMainActivity extends AppCompatActivity {
    private List<Person> persons;
    private Store<Action<ActionType, Person>, List<Person>> store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactive_main);
        initializeData();
        Anvil.mount(findViewById(R.id.root), new Anvil.Renderable() {
            @Override
            public void view() {
                Recycler.view(new RenderableView(getApplicationContext()) {
                                  @Override
                                  public void view() {
                                      padding(0, dip(60), 0, 0);
                                      Recycler.layoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                      Recycler.hasFixedSize(true);
                                      Recycler.itemAnimator(new DefaultItemAnimator());
                                      Recycler.adapter(new Recycler.Adapter() {

                                                           @Override
                                                           public int getItemCount() {
                                                               return persons.size();
                                                           }

                                                           @Override
                                                           public void view(final RecyclerView.ViewHolder holder) {
                                                               linearLayout(new Anvil.Renderable() {
                                                                   @Override
                                                                   public void view() {
                                                                       orientation(LinearLayout.HORIZONTAL);
                                                                       padding((int) getResources().getDimension(R.dimen.card_padding));

                                                                       textView(new Anvil.Renderable() {
                                                                           @Override
                                                                           public void view() {
                                                                               id(1);
                                                                               text(persons.get(holder.getAdapterPosition()).getName());
                                                                           }
                                                                       });

                                                                       textView(new Anvil.Renderable() {
                                                                           @Override
                                                                           public void view() {
                                                                               toRightOf(1);
                                                                               padding(15, 0, 0, 0);
                                                                               id(2);
                                                                               text(persons.get(holder.getAdapterPosition()).getAge());
                                                                           }
                                                                       });
                                                                       imageView(new Anvil.Renderable() {
                                                                           @Override
                                                                           public void view() {
                                                                               toRightOf(2);
                                                                               imageResource(persons.get(holder.getAdapterPosition()).getPhotoId());
                                                                               size(300, 300);
                                                                               scaleType(ImageView.ScaleType.FIT_CENTER);
                                                                           }
                                                                       });
                                                                   }
                                                               });

                                                               onClick(new OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       store.dispatch(new Action<>(ActionType.DELETE_PERSON, persons.get(holder.getAdapterPosition())));
                                                                   }
                                                               });
                                                           }
                                                       }
                                      );
                                  }
                              }
                );

                imageButton(new Anvil.Renderable() {
                    @Override
                    public void view() {
                        imageResource(android.R.drawable.ic_input_add);
                        bottom(dip(0));
                        onClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showAddItemDialog();
                            }
                        });
                    }
                });
            }
        });
        Anvil.render();
        this.store = new Store<>(this.persons);
        this.store.addReducer(this.reducer);
        this.store.subscribeView(this.observer);

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
            persons = store.getState();
            Anvil.render();
        }
    };

    private void initializeData() {
        List<Person> creationList = new ArrayList<>();
        creationList.add(new Person("Emma Watson", "23 years old", R.drawable.emma_watson));
        creationList.add(new Person("Lavery Maiss", "25 years old", R.drawable.holly));
        creationList.add(new Person("Lillie Watts", "35 years old", R.drawable.vurst));
        persons = Collections.unmodifiableList(creationList);
    }

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
