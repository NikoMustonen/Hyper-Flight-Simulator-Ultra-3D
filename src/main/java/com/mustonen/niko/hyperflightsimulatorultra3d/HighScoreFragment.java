package com.mustonen.niko.hyperflightsimulatorultra3d;

import android.view.*;
import android.widget.*;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Class for displaying fragment which stores list of 10 best high scores.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
public class HighScoreFragment extends Fragment {

    /**
     * Stores list view which stores all the best players around the globe.
     */
    private ListView listView;

    /**
     * Stores high score players and their point as a string array.
     */
    private String[] highScoreList;

    /**
     * Holds view that displays highscore screen.
     */
    private View view;

    /**
     * Stores constant value for defining secret splitter for point string array.
     */
    static final String SPLITTER = "erreoignerogidsbffsfbjsdfbdsjhfbsdjf";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_list, container, false);

        return view;
    }

    /**
     * Sets new high score element for the fragment.
     *
     * @param array Array of json high score objects.
     */
    public void setHighScoreList(JSONArray array) {
        highScoreList = new String[array.length()];
        for (int i = 0; i < highScoreList.length; i++) {
            try {
                highScoreList[i] = "" + (array.getJSONObject(i).getString("name")) + SPLITTER
                        + (array.getJSONObject(i).getInt("points"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listView = (ListView) view.findViewById(R.id.listview);
        if (listView.getAdapter() != null) {
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        } else {
            ArrayAdapter adapter = new MyArrayAdapter(getActivity(), highScoreList);
            listView.setAdapter(adapter);
        }
    }
}

/**
 * Class for converting high scores to list.
 *
 * @author Niko Mustonen mustonen.niko@gmail.com
 * @version %I%, %G%
 * @since 1.8
 */
class MyArrayAdapter extends ArrayAdapter<String> {

    /**
     * Constructor for MyAdapter.
     *
     * @param context Activity context.
     * @param data Data.
     */
    public MyArrayAdapter(Context context, String[] data) {
        super(context, R.layout.list_row, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_row, parent, false);

        String[] pair = getItem(position).split(HighScoreFragment.SPLITTER);
        TextView title = (TextView) customView.findViewById(R.id.row_title);
        TextView points = (TextView) customView.findViewById(R.id.points);

        title.setText(position + 1 + ": " + pair[0]);
        points.setText(pair[1]);

        return customView;
    }
}
