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
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private final List<TriviaScore> triviaScores;
    private final TriviaRoomViewModel tvModel;

    public ScoreAdapter(TriviaRoomViewModel tvModel) {
        this.tvModel = tvModel;
        this.triviaScores = tvModel.playerScores.getValue();
    }

    @NonNull
    @Override
    public ScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.top10_score_item, parent, false);
        return new ViewHolder(listItem, tvModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreAdapter.ViewHolder holder, int position) {
        final TriviaScore triviaScore = triviaScores.get(position);
        holder.tvUsername.setText(triviaScore.getPlayerName());
        holder.tvUserScore.setText(triviaScore.getScoreString());
        holder.tvUserRank.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return triviaScores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvUsername;
        public TextView tvUserScore;
        public TextView tvUserRank;

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
