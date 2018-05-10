package com.lis.qr_client.rest;

import android.os.Bundle;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import com.lis.qr_back.R;
import com.lis.qr_client.pojo.Greeting;
import com.lis.qr_client.pojo.User;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "before connection in OnStart");
        new HttpRequestTask().execute();
        Log.i("MainActivity", "after connection in OnStart");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Log.i("MainActivity", "before connection in InOptMenu");
            new HttpRequestTask().execute();
            Log.i("MainActivity", "after connection in InOptMenu");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... voids) {
            try {
                //generally, localhost is 10.0.2.2 (for the loopback), but in genymotion 10.0.3.2
                //in my case its also 192.168.234.2, also 172.36.2.102. Seems to my emulator is using my wifi connection
                final String url = "http://10.0.3.2:8090/users/Som";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.getForObject(url, User.class);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            TextView username = findViewById(R.id.username_value);
            TextView email = findViewById(R.id.email_value);
            username.setText(user.getUsername());
            email.setText(user.getEmail());
        }
    }
}
