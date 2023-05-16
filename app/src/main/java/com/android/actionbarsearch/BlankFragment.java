package com.android.actionbarsearch;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class BlankFragment extends ListFragment implements
        TextView.OnEditorActionListener, SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {
    private static final String STATE_QUERY="q";
    private static final String STATE_MODEL="m";
    private static final String[] items= { "lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi",
            "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam",
            "vel", "erat", "placerat", "ante", "porttitor", "sodales",
            "pellentesque", "augue", "purus" };
    private ArrayList<String> words=null;
    private ArrayAdapter<String> adapter=null;
    private CharSequence initialQuery=null;
    private SearchView sv=null;


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }*/


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            initAdapter(null);
        }
        else {
            initAdapter(savedInstanceState.getStringArrayList(STATE_MODEL));
            initialQuery=savedInstanceState.getCharSequence(STATE_QUERY);
        }



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        if (!sv.isIconified()) {
            state.putCharSequence(STATE_QUERY, sv.getQuery());
        }

        state.putStringArrayList(STATE_MODEL, words);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actions, menu);



        configureSearchView(menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
            adapter.add(v.getText().toString());
            v.setText("");

            InputMethodManager imm=
                    (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        return(true);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            adapter.getFilter().filter("");
        }
        else {
            adapter.getFilter().filter(newText.toString());
        }

        return(true);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return(false);
    }

    @Override
    public boolean onClose() {
        adapter.getFilter().filter("");

        return(true);
    }


    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity(), adapter.getItem(position),
                Toast.LENGTH_LONG).show();
    }

    private void configureSearchView(Menu menu) {
        MenuItem search=menu.findItem(R.id.search);

        sv=(SearchView)search.getActionView();
        sv.setOnQueryTextListener(this);
        sv.setOnCloseListener(this);
        sv.setSubmitButtonEnabled(false);
        sv.setIconifiedByDefault(true);

        if (initialQuery != null) {
            sv.setIconified(false);
            search.expandActionView();
            sv.setQuery(initialQuery, true);
        }
    }
    private void initAdapter(ArrayList<String> startingPoint) {
        if (startingPoint == null) {
            words=new ArrayList<String>();

            for (String s : items) {
                words.add(s);
            }
        }
        else {
            words=startingPoint;
        }

        adapter=
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        words);

        setListAdapter(adapter);
    }
}