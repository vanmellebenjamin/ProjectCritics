package mouselab.projectcriticsandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import mouselab.projectcriticsandroid.R;

/**
 * Created by Jef on 10/07/2015.
 */
public class TopicsFragment extends Fragment {
    public static final String ARG_OBJECT = "Topics";
    private TableLayout tableTopics;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate( R.layout.fragment_topics, container, false);
        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.text1)).setText(args.getString(ARG_OBJECT));
        tableTopics = (TableLayout) rootView.findViewById(R.id.tableTopics);
        getResources();



        return rootView;
    }
}
