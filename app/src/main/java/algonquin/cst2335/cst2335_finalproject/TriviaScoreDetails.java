package algonquin.cst2335.cst2335_finalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import algonquin.cst2335.cst2335_finalproject.databinding.FragmentTriviaScoreDetailsBinding;

/**
 * Class for the Triva Score detail fragment, that shows the player name, score, time played,
 * and delete button if applicable.
 */
public class TriviaScoreDetails extends Fragment {

    private static TriviaScoreDetails fragment;
    private TriviaRoomViewModel tvModel;
    private TriviaScoreAdapter adapter;
    private TriviaDatabase tvDB;

    /**
     * Constructor for the Trivia Score Details Fragment that initializes the fragment with
     * a TriviaScore selected in the Trivia Main Activity top 10 list.
     *
     * @param tvModel The TriviaRoomViewModel with the scores information.
     */
    private TriviaScoreDetails(TriviaRoomViewModel tvModel, TriviaScoreAdapter adapter, TriviaDatabase tvDB) {
        this.tvModel = tvModel;
        this.adapter = adapter;
        this.tvDB = tvDB;
    }

    /**
     * Method that creates a new instance of the TriviaScoreDetail fragment based on the specified
     * trivia score.
     *
     * @param tvModel The TriviaRoomViewModel with the score information to be loaded.
     * @return A new instance of fragment TriviaScoreDetails.
     */
    public static TriviaScoreDetails newInstance(TriviaRoomViewModel tvModel, TriviaScoreAdapter adapter, TriviaDatabase tvDB) {
        if (fragment == null) {
            fragment = new TriviaScoreDetails(tvModel, adapter, tvDB);
        }
        return fragment;
    }

    /**
     * onCreateView method for the score details fragment that creates the fragment
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The layout where the fragment will be set.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTriviaScoreDetailsBinding binding = FragmentTriviaScoreDetailsBinding.inflate(inflater);
        TriviaScore selectedScore = tvModel.selectedScore.getValue();

        binding.playerNameDetail.setText(selectedScore.getPlayerName());
        binding.userScoreDetail.setText(selectedScore.getScoreString());
        binding.gameDate.setText(selectedScore.getGameDateString());

        binding.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(getString(R.string.delete_score));
            builder.setMessage(getString(R.string.delete_msg));
            builder.setNegativeButton(getString(R.string.cancel), (dialog, cl) -> {});

            builder.setPositiveButton(getString(R.string.yes), (dialog, cl) -> {
                int position = tvModel.playerScores.getValue().indexOf(selectedScore);
                tvDB.deleteTriviaScore(selectedScore);
                tvModel.playerScores.getValue().remove(selectedScore);
                adapter.notifyDataSetChanged();
                getParentFragmentManager().popBackStack();
            });
            builder.create().show();
        });

        binding.btnOk.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });

        return binding.getRoot();
    }
}