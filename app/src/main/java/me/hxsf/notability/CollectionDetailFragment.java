package me.hxsf.notability;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;

import java.util.ArrayList;

import me.hxsf.notability.data.Note;
import me.hxsf.notability.dummy.DummyContent;

/**
 * A fragment representing a single Collection detail screen.
 * This fragment is either contained in a {@link CollectionListActivity}
 * in two-pane mode (on tablets) or a {@link CollectionDetailActivity}
 * on handsets.
 */
public class CollectionDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    private ArrayList<Note> noteArrayList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CollectionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.collection_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.collection_detail)).setText(mItem.details);
            NoteListViewAdapter noteListViewAdapter = new NoteListViewAdapter(noteArrayList, .this);
            ListView listView = (ListView) findViewById(R.id.collection_detail);
            listView.setAdapter(NoteListViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        }

        return rootView;
    }
}
