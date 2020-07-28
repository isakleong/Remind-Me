package com.example.arlyston.proyekandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;



/**
 * Created by Arlyston on 28/10/2018.
 */



public class WordListAdapter extends
        RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private final LinkedList<Activity> mActivities;
    private LayoutInflater mInflater;
    private Context context;



    public WordListAdapter(Context context, LinkedList<Activity> activityList) {
        mInflater = LayoutInflater.from(context);
        mActivities = activityList;
        this.context = context;
    }


    public class WordViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener{
        public final TextView wordItemView;
        public final TextView wordDateView;
        public final TextView wordTimeView;
        public final Button buttonItemView;
        final WordListAdapter mAdapter;

        public WordViewHolder(View itemView, WordListAdapter adapter){
            super(itemView);
            wordItemView = (TextView) itemView.findViewById(R.id.word);
            buttonItemView = (Button) itemView.findViewById(R.id.button);
            wordDateView = (TextView) itemView.findViewById(R.id.date);
            wordTimeView = (TextView) itemView.findViewById(R.id.time);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            String mTitle = mActivities.get(mPosition).getName();
            Date mDate = mActivities.get(mPosition).getConvdate();
            String mPriority = mActivities.get(mPosition).getPriority();

            mAdapter.notifyDataSetChanged();
            Intent i = new Intent(context, Edit.class);
            i.putExtra("mPosition", mPosition);
            i.putExtra("mTitle", mTitle);
            i.putExtra("mDate", mDate);
            i.putExtra("mPriority", mPriority);

            context.startActivity(i);
        }

        @Override
        public boolean onLongClick(View v) {

            AlertDialog.Builder builder;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            int mPosition = getLayoutPosition();
                            MainActivity.users.child(String.valueOf(MainActivity.mActivity.get(mPosition).getId())).removeValue();
                            MainActivity.mActivity.remove(mPosition);
                            MainActivity.mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View mItemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(final WordListAdapter.WordViewHolder holder, final int position) { ////gettt
        final String mCurrent = mActivities.get(position).getName();
        final Date mCurrentDate = mActivities.get(position).getConvdate();
        final String mCurrentPriority = mActivities.get(position).getPriority();


        holder.wordItemView.setText(mCurrent);

        Format formatter2 = new SimpleDateFormat("MM/dd/yy"); //date to string
        Format formatter3 = new SimpleDateFormat("HH:mm"); //date to string
        String s = formatter2.format(mCurrentDate);
        String h = formatter3.format(mCurrentDate);

        holder.wordDateView.setText(s);
        holder.wordTimeView.setText(h);
        Log.d("my hero", ""+mCurrentPriority);

        if(mCurrentPriority.equals("High Priority")){
            holder.buttonItemView.setBackgroundColor(Color.RED);
            Log.d("my hero", "high");
        }
        else if(mCurrentPriority.equals("Medium Priority")){
            holder.buttonItemView.setBackgroundColor(Color.YELLOW);
            Log.d("my hero", "med");
        }
        else if(mCurrentPriority.equals("Low Priority")){
            holder.buttonItemView.setBackgroundColor(Color.GREEN);
            Log.d("my hero", "low");
        }

    }

    @Override
    public int getItemCount() {
        return mActivities.size();
    }

}

