package com.example.myapplication3;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication3.R;
import com.example.myapplication3.model.quotes;

import java.util.ArrayList;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {

    private List<quotes> quoteList;

    public QuoteAdapter() {
        this.quoteList = new ArrayList<>();
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_item, parent, false);
        return new QuoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        holder.bind(quoteList.get(position));
    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }

    public void setQuote(quotes quote) {
        this.quoteList.clear();
        this.quoteList.add(quote);
        notifyDataSetChanged();
    }

    public void setQuoteList(List<quotes> quoteList) {
        this.quoteList = quoteList;
        notifyDataSetChanged();
    }

    public static class QuoteViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewQuote;
        private TextView textViewAuthor;
        private TextView textViewCreatedDate;
        private TextView textViewQuoteId;
        private Button buttonCopyID;
        private Button buttonCopyQuote;

        public QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuote = itemView.findViewById(R.id.textViewQuote);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewCreatedDate = itemView.findViewById(R.id.textViewCreatedDate);
            textViewQuoteId = itemView.findViewById(R.id.textViewQuoteId);
            buttonCopyID = itemView.findViewById(R.id.button5);
            buttonCopyQuote = itemView.findViewById(R.id.button6);

            itemView.setOnClickListener(new View.OnClickListener() {
                private boolean isColorChanged = false;

                public void onClick(View v) {
                    isColorChanged = !isColorChanged;
                    if (isColorChanged) {
                        itemView.setBackgroundColor(Color.RED);
                    } else {
                        itemView.setBackgroundColor(Color.BLUE);
                    }
                }
            });

            // Set LongClickListener for TextViews to enable copying
            textViewQuote.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showContextMenu(textViewQuote.getText().toString(), v.getContext());
                    return false;
                }
            });

            textViewAuthor.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showContextMenu(textViewAuthor.getText().toString(), v.getContext());
                    return false;
                }
            });

            textViewCreatedDate.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showContextMenu(textViewCreatedDate.getText().toString(), v.getContext());
                    return false;
                }
            });

            // Set ClickListener for Copy ID button
            buttonCopyID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyToClipboard(textViewQuoteId.getText().toString(), v.getContext());
                }
            });

            // Set ClickListener for Copy Quote button
            buttonCopyQuote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyToClipboard(textViewQuote.getText().toString(), v.getContext());
                }
            });
        }

        private void showContextMenu(String text, Context context) {
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("Select Action");
                    menu.add(0, v.getId(), 0, "Copy").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            copyToClipboard(text, context);
                            return true;
                        }
                    });
                }
            });
        }


        private void copyToClipboard(String text, Context context) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        }

        public void bind(quotes quote) {
            if (quote != null) {
                if (quote.getQuote() != null) {
                    textViewQuote.setText(quote.getQuote());
                } else {
                    textViewQuote.setText("Default Quote Text");
                }

                if (quote.getAuthor() != null) {
                    textViewAuthor.setText(quote.getAuthor());
                } else {
                    textViewAuthor.setText("Default Author");
                }

                if (quote.getCreatedAt() != null) {
                    textViewCreatedDate.setText(quote.getCreatedAt());
                } else {
                    textViewCreatedDate.setText("Default Date");
                }

                // Set quote ID
                if (quote.getId() != null) {
                    textViewQuoteId.setText(quote.getId());
                } else {
                    textViewQuoteId.setText("Default ID");
                }
            }
        }
    }
}
