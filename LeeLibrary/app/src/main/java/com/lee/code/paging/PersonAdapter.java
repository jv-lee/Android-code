package com.lee.code.paging;

import android.annotation.SuppressLint;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lee.code.R;


public class PersonAdapter extends PagedListAdapter<Person, PersonAdapter.PersonViewHolder> {

    PersonAdapter() {
        super(diffCallback);
    }

    private static DiffUtil.ItemCallback<Person> diffCallback = new DiffUtil.ItemCallback<Person>() {
        @Override
        public boolean areItemsTheSame(@NonNull Person person, @NonNull Person t1) {
            return person.id == t1.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Person person, @NonNull Person t1) {
            @SuppressLint("DiffUtilEquals") boolean equals = person.equals(t1);
            return equals;
        }
    };

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder personViewHolder, int position) {
        personViewHolder.bindData(getItem(position));
    }


    class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
        }

        public void bindData(Person person) {
            tvText.setText(person.name);
        }
    }
}
