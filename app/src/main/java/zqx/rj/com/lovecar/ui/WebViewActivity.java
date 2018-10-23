package zqx.rj.com.lovecar.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import zqx.rj.com.lovecar.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        LinearLayout webView = findViewById(R.id.ll_webview);

        String url = getIntent().getStringExtra("url");

        AgentWeb agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
    }
}
