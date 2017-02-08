package test.mahendran.testing;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import test.mahendran.testing.model.Question;

public class MainActivity extends AppCompatActivity {
    private int mCurrentQuestionIndex = 0;
    private ArrayList<Question> mQuestions;
    private Menu mMenu;
    private android.app.FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getFragmentManager();
        addQuestionFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void addQuestionFragment() {
        try {
            mQuestions = new Gson().fromJson(Utils.getQuestion(this), new TypeToken<ArrayList<Question>>(){}.getType());
            updateQuestionView(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateQuestionView(int questionIndex) {
        QuestionFragment fragment = QuestionFragment.newInstance();
        mCurrentQuestionIndex = questionIndex;
        fragment.setQuestion(mQuestions.get(questionIndex));
        mFragmentManager.beginTransaction().add(R.id.container, fragment).addToBackStack("question"+mCurrentQuestionIndex).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.question_action) {
            if (mCurrentQuestionIndex == mQuestions.size() - 1)
                actionDone();
            else
                actionNext();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager != null && mFragmentManager.getBackStackEntryCount() > 1) {
            mCurrentQuestionIndex--;
            closeActiveFragment();
        } else if (mFragmentManager != null && mFragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }

        updateActionTitle();
    }

    public void closeActiveFragment() {
        if (mFragmentManager != null && mFragmentManager.getBackStackEntryCount() > 0)
            mFragmentManager.popBackStack();
    }

    private void actionNext() {
        updateQuestionView(mCurrentQuestionIndex + 1);
        updateActionTitle();
    }

    private void updateActionTitle() {
        MenuItem action = mMenu.findItem(R.id.question_action);
        if (mCurrentQuestionIndex == mQuestions.size() - 1) {
            action.setTitle("Done");
        } else {
            action.setTitle("Next");
        }
    }

    private void actionDone() {

    }
}
