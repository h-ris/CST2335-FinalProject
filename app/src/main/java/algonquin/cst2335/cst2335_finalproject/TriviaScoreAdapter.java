package algonquin.cst2335.cst2335_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Class for the Trivia Score adapter to work with the Recycler View for the Top 10 list.
 */
public class TriviaScoreAdapter extends RecyclerView.Adapter<TriviaScoreAdapter.ViewHolder> {

    private final List<TriviaScore> triviaScores;
    private final TriviaRoomViewModel tvModel;

    /**
     * Constructor for the TriviaScoreAdapter.
     *
     * @param tvModel The TriviaRoomViewModel to interact with.
     */
    public TriviaScoreAdapter(TriviaRoomViewModel tvModel) {
        this.tvModel = tvModel;
        this.triviaScores = tvModel.playerScores.getValue();
    }

    @NonNull
    @Override
    public TriviaScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.top10_score_item, parent, false);
        return new ViewHolder(listItem, tvModel);
    }

    @Override
    public void onBindViewHolder(@NonNull TriviaScoreAdapter.ViewHolder holder, int position) {
        final TriviaScore triviaScore = triviaScores.get(position);
        holder.tvUsername.setText(triviaScore.getPlayerName());
        holder.tvUserScore.setText(triviaScore.getScoreString());
        holder.tvUserRank.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return triviaScores.size();
    }

    /**
     * ViewHolder class for a single trivia score item.
     * This class handles the binding of data to the item view and sets up click listeners.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvUsername;
        public TextView tvUserScore;
        public TextView tvUserRank;

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The item view for the trivia score.
         * @param tvModel  The TriviaRoomViewModel for interaction.
         */
        public ViewHolder(View itemView, TriviaRoomViewModel tvModel) {
            super(itemView);
            this.tvUsername = itemView.findViewById(R.id.userNameTop10);
            this.tvUserScore = itemView.findViewById(R.id.userScoreTop10);
            this.tvUserRank = itemView.findViewById(R.id.userRank);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                TriviaScore selected = tvModel.playerScores.getValue().get(position);

                if (selected != null) {
                    tvModel.selectedScore.postValue(selected);
                }
            });
        }
    }
}
