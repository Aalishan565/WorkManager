package ayesha.workmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {
    OneTimeWorkRequest oneTimeWorkRequest;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Data data = new Data.Builder().putString("1", "Allu").build();
        //For setting the constraints
        //Constraints constraints = new Constraints.Builder().setRequiresCharging(false).build();
        oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class).setInputData(data).build();
        mTvResult = findViewById(R.id.textView);

    }

    public void startWork(View view) {
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (null != workInfo) {
                    if (workInfo.getState().isFinished()) {
                        Data data = workInfo.getOutputData();
                        String status = data.getString("Result");
                        mTvResult.append(status + "\n");
                    }
                }

            }
        });
    }
}
