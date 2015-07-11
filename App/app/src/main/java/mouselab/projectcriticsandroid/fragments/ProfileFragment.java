package mouselab.projectcriticsandroid.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mouselab.projectcriticsandroid.R;

/**
 * Created by Jef on 10/07/2015.
 */
public class ProfileFragment extends Fragment {
    public static final String ARG_OBJECT = "Profile";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_profile, container, false);
        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.text1)).setText(
                args.getString(ARG_OBJECT));
        return rootView;
    }
}
