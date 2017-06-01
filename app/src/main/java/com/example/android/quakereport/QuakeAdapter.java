package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.quakereport.R.id.magnitude;

/**
 * Created by RAYNOLD on 5/13/2017.
 */

public class QuakeAdapter extends ArrayAdapter<Quake> {

    public QuakeAdapter(Activity context, ArrayList<Quake> earthquakes) {

        super(context, 0, earthquakes );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Quake} object located at this position in the list
        Quake currentQuake = getItem(position);


        // Find the TextView in the list_item.xml layout with the ID text1
        TextView magnitudeTextView = (TextView) listItemView.findViewById(magnitude);
        // Get the english translation from the current Word object and
        // set this text on the english translation  TextView
//        DecimalFormat formatter = new DecimalFormat("0.0");
//        double magDouble = currentQuake.getMagnitude();
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentQuake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        String output = formatMagnitude(currentQuake.getMagnitude());

        magnitudeTextView.setText(output);

        String separate = currentQuake.getLocation();
        if (separate.contains("of")) {
            String[] separateArray = separate.split("(?<=of )");

            TextView locationOfTextView = (TextView) listItemView.findViewById(R.id.location_of);
            // Get the miwok translation from the current Word object and
            // set this text on the miwok translation  TextView
            locationOfTextView.setText(separateArray[0]);

            // Find the TextView in the list_item.xml layout with the ID version_number
            TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);
            // Get the miwok translation from the current Word object and
            // set this text on the miwok translation  TextView
            locationTextView.setText(separateArray[1]);

        } else {
            String nearOf = "Near the";
            TextView locationOfTextView = (TextView) listItemView.findViewById(R.id.location_of);
            // Get the miwok translation from the current Word object and
            // set this text on the miwok translation  TextView
            locationOfTextView.setText(nearOf);

            TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);
            // Get the miwok translation from the current Word object and
            // set this text on the miwok translation  TextView
            locationTextView.setText(separate);
        }


        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentQuake.getDate());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Get the miwok translation from the current Word object and
        // set this text on the miwok translation  TextView
        dateTextView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);


        return listItemView;

    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

}
