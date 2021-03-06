package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * An adapter so users can view all the comments in the (larger) event view
 */
public class CommentListAdapter extends ArrayAdapter<ParseObject>{
    private final Context context;
    private final ArrayList<ParseObject> data;
    private final HashMap<String, String> authorToImage = new HashMap<>(8);
    private final HashMap<String, String> userToName = new HashMap<>(8);
    private final int layoutResourceId;
    private final String eventAuthor;
    private final String[] names = {"Purple Boat", "Orange Balloon", "Red Sox", "Yellow Anchor", "Black UFO","Blue Compass","Red Map","Blue Paw"};


    public CommentListAdapter(Context context,  int layoutResourceId, ArrayList<ParseObject> data, String eventAuthor) {

        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.eventAuthor = eventAuthor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //The View that gets the list_row_view layout
        View row = convertView;

        //Class that holds all our types of views
        ViewHolder holder;

        if(row == null) {  //If the row of the ViewGroup is empty then add a new item using list_row_view layout


            //Gets the context of the place that calls this EventListAdapter
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            //Assigns the Views to its type of view
            holder = new ViewHolder();
            holder.commentName = (TextView) row.findViewById(R.id.comment_row_title);

            holder.commentIcon = (ImageView) row.findViewById(R.id.comment_icon);

            holder.commentContent = (TextView) row.findViewById(R.id.comment_row_conetent);
            holder.commentDate = (TextView) row.findViewById(R.id.comment_row_date);


            row.setTag(holder);
        }
        else {  //If row is not empty then just return that View's tag
            holder = (ViewHolder)row.getTag();
        }


        //Gets Event object in the ArrayList<Event> at that current position
        ParseObject comment = data.get(position);

        // To check if display across two days

        if(comment != null) {
            SimpleDateFormat timeOfComment = new SimpleDateFormat(" dd HH:ss");
            String monthOfYear = "";
            int month = comment.getCreatedAt().getMonth();
            switch(month){
                case 0: monthOfYear = "Jan"; break;
                case 1: monthOfYear = "Feb"; break;
                case 2: monthOfYear = "Mar"; break;
                case 3: monthOfYear = "Apr"; break;
                case 4: monthOfYear = "May"; break;
                case 5: monthOfYear = "Jun"; break;
                case 6: monthOfYear = "Jul"; break;
                case 7: monthOfYear = "Aug"; break;
                case 8: monthOfYear = "Sep"; break;
                case 9: monthOfYear = "Oct"; break;
                case 10: monthOfYear = "Nov"; break;
                case 11: monthOfYear = "Dec"; break;
                default: break;
            }
            String dateToDisplay = timeOfComment.format(comment.getCreatedAt());


            // Sets the icons
            // If your previous comment keeps consistent
            if(comment.getString("postId").equals(MainActivity.currentUser.getUsername())) {
                holder.commentIcon.setImageResource(R.drawable.you);
                holder.commentName.setText("You");

            }

            else if(comment.getString("postId").equals(eventAuthor)){
                holder.commentIcon.setImageResource(R.drawable.op);
                holder.commentName.setText("Original Poster");


            } else{ // hashes user ids
                if(authorToImage.containsKey(comment.getString("postId"))){
                    String str = authorToImage.get(comment.getString("postId"));
                    holder.commentIcon.setImageResource(getResourceID(str, "drawable", getContext()));
                    holder.commentName.setText(userToName.get(comment.getString("postId")));



                }else{
                    final Random rnd = new Random();
                    final int randNum = rnd.nextInt(7);
                    final String str = "img_" + randNum;
                    if(authorToImage.size() < 8){
                        authorToImage.put(comment.getString("postId"), str);
                        userToName.put(comment.getString("postId"), names[randNum]);


                    }
                    holder.commentIcon.setImageResource(getResourceID(str, "drawable", getContext()));
                    holder.commentName.setText(userToName.get(comment.getString("postId")));

                }

            }
            //Sets the views Comment
            holder.commentContent.setText(comment.getString("comment"));
            holder.commentDate.setText(monthOfYear + dateToDisplay);
        }
        return row;

    }

    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }



    static class ViewHolder
    {
        ImageView commentIcon;
        TextView commentName;
        TextView commentContent;
        TextView commentDate;
    }
}
