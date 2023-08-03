package algonquin.cst2335.trivia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;

/**
 * Class for the Trivia Score adapter to work with the Recycler View for the Top 10 list.
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private List<TriviaScore> triviaScores;

    public ScoreAdapter(List<TriviaScore> triviaScores) {
        this.triviaScores = triviaScores;
    }

    @NonNull
    @Override
    public ScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.top10_score_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreAdapter.ViewHolder holder, int position) {
        final TriviaScore triviaScore = triviaScores.get(position);
        holder.tvUsername.setText(triviaScore.getUserName());
        holder.tvUserScore.setText(triviaScore.getScoreString());
    }

    @Override
    public int getItemCount() {
        return triviaScores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvUsername;
        public TextView tvUserScore;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvUsername = (TextView) itemView.findViewById(R.id.userNameTop10);
            this.tvUserScore = (TextView) itemView.findViewById(R.id.userScoreTop10);
        }
    }
}
