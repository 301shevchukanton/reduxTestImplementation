package com.example.ash.reduxlmplementation;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PersonViewHolder> {
    List<Person> persons;


    RecyclerViewItemClickListener listener;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;
        RecyclerViewItemClickListener listener;
        Person person;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personAge = (TextView) itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(person);
                }
            });
        }

        public void bind(RecyclerViewItemClickListener listener, Person person) {
            this.listener = listener;
            this.person = person;
        }
    }

    public RecyclerViewAdapter(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public void setListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        pvh.bind(listener, persons.get(i));
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).getName());
        personViewHolder.personAge.setText(persons.get(i).getAge());
        personViewHolder.personPhoto.setImageResource(persons.get(i).getPhotoId());
        personViewHolder.bind(listener, persons.get(i));
    }


}