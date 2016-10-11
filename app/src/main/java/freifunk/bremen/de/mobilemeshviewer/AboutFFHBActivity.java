package freifunk.bremen.de.mobilemeshviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutFFHBActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.contact_button_mail)
    Button buttonMail;
    @BindView(R.id.contact_button_irc)
    Button buttonChat;
    @BindView(R.id.contact_button_twitter)
    Button buttonTwitter;
    @BindView(R.id.contact_button_facebook)
    Button buttonFacebook;
    @BindView(R.id.contact_button_website)
    Button buttonWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ffhb);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_about_ffhb));

        buttonMail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + getString(R.string.contact_e_mail)));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.contact_e_mail_message)));
            }
        });

        buttonChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.contact_chat)));
                startActivity(Intent.createChooser(webIntent, getString(R.string.contact_chat_message)));
            }
        });

        buttonTwitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.contact_twitter)));
                startActivity(Intent.createChooser(webIntent, getString(R.string.contact_twitter_message)));
            }
        });

        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.contact_facebook)));
                startActivity(Intent.createChooser(webIntent, getString(R.string.contact_facebook_message)));
            }
        });

        buttonWebsite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.contact_website)));
                startActivity(Intent.createChooser(webIntent, getString(R.string.contact_website_message)));
            }
        });
    }
}