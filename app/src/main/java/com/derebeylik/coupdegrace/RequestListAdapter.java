package com.derebeylik.coupdegrace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/12/2017.
 */

public class RequestListAdapter extends ArrayAdapter {
    public RequestListAdapter(Context context, HelpRequest[] helpRequests) {
        super(context, 0,helpRequests);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HelpRequest request = (HelpRequest)getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.help_request_button, parent, false);
        }
        // Lookup view for data population
        Button RequestButton = (Button) convertView.findViewById(R.id.RequestButton);

        // Populate the data into the template view using the data object
        RequestButton.setText(request.cityName);
        RequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

}
