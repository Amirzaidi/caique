package v6.caique;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

class ChatTypingAdapter extends ArrayAdapter<CacheChats.MessageStructure> {

    private String ChatId;
    private LayoutInflater vi;
    private Context context;
    private ArrayList<CacheChats.MessageStructure> List;

    public ChatTypingAdapter(Context c, @LayoutRes int resource, String ChatId, ArrayList<CacheChats.MessageStructure> List) {
        super(c, resource, List);
        this.List = List;
        this.ChatId = ChatId;
        vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = c;
    }

    public void Refill()
    {
        List.clear();

        long unixTime = System.currentTimeMillis() / 1000L;
        for (CacheChats.MessageStructure Data : CacheChats.Loaded.get(ChatId).Typing.values())
        {
            if (Data.Date >= unixTime - 5)
            {
                List.add(Data);
            }
        }

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {

        if (row == null)
        {
            row = vi.inflate(R.layout.list_item_message, parent, false);
        }

        CacheChats.ChatStructure Chat = CacheChats.Loaded.get(ChatId);
        if (Chat.Typing.size() > position)
        {
            CacheChats.MessageStructure Data = List.get(position);

            TextView MessageSender = (TextView) row.findViewById(R.id.messageItemSender);
            TextView Message = (TextView) row.findViewById(R.id.messageItem);

            Message.setText(Data.Content);

            MessageSender.setVisibility(VISIBLE);
            MessageSender.setText(CacheChats.Name(Data.Sender) + " is typing..");

            ImageView imageView = (ImageView) row.findViewById(R.id.userdp);
            imageView.setImageDrawable(null);
            imageView.setVisibility(INVISIBLE);
            imageView.getLayoutParams().height = 0;
            imageView.requestLayout();
        }

        return row;
    }
}