package com.example.mathematics;

import static com.example.mathematics.GameActivity.BEGINNER_USER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {
    private List<Answer> answers;

    public AnswersAdapter(List<Answer> answers) {
        this.answers = answers;
    }

    @NonNull
    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_answer_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.ViewHolder holder, int position) {
        Answer answer = answers.get(position);
        holder.bind(answer, position);

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View row;
        private TextView numberOfQuestion;
        private TextView questionType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.checkID);
            numberOfQuestion = itemView.findViewById(R.id.numberOfQuestion);
            questionType = itemView.findViewById(R.id.questionType);
            row = itemView;

        }

        public void bind(Answer answer, int position) {
            imageView.setImageResource(answer.isAnswer() ? R.drawable.ic_baseline_check_24 : R.drawable.ic_baseline_clear_24);
            numberOfQuestion.setText("Question " + (position + 1));
            questionType.setText(getQuestionText(answer.getQuestionType()));

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getNextIntent(answer, view.getContext());
                    view.getContext().startActivity(intent);

                }
            });

        }
        private String getQuestionText(QuestionType type){
            switch (type) {
                case VERBAL_QUESTIONS:
                    return "Verbal Exercise";


                case FRACTIONS:
                    return "Fractions Exercise";
                case DECIMAL_FRACTIONS:
                    return "Decimal Fraction Exercise";
                case WHOLE_NUMBERS:
                    return "Whole Numbers Exercise";
                default:
                    throw new RuntimeException();
            }
        }

        public Intent getNextIntent(Answer answer, Context context) {
            Intent intent;
            int selectedLevel=answer.getLevel();
            switch (answer.getQuestionType()) {
                case VERBAL_QUESTIONS:
                    intent = new Intent(context, AddNewQuestionActivity.class);
                    intent.putExtra("Level", selectedLevel == BEGINNER_USER);
                    intent.putExtra("Mode", "Answer");
                    break;
                case FRACTIONS:
                    intent = new Intent(context, FractionExerciseActivity.class);
                    intent.putExtra("Level", selectedLevel);
                    break;
                case DECIMAL_FRACTIONS:
                    intent = new Intent(context, DecimalFractionExerciseActivity.class);
                    intent.putExtra("Level", selectedLevel);
                    break;
                case WHOLE_NUMBERS:
                    intent = new Intent(context, SimpleNumbersExerciseActivity.class);
                    intent.putExtra("Level", selectedLevel);
                    break;
                default:
                    throw new RuntimeException();
            }
            intent.putExtra("answer",answer);


            return intent;

        }

    }
}
