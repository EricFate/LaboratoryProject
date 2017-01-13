package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.BaseApplication;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.glide.GlideRoundTransform;
import hl.iss.whu.edu.laboratoryproject.manager.SmackManager;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.BaseFragment;
import hl.iss.whu.edu.laboratoryproject.utils.FragmentFactory;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.rg_main)
    RadioGroup rgMain;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private ImageView image;
    FragmentManager mFragmentManager;
    private String[] test = {"aaa", "bbb", "ccc"};
    private TextView tvsigniture;
    private TextView tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initSearchView();
        setup();
    }

    @Override
    protected void onDestroy() {
        if (SmackManager.getConnection().isConnected())
            SmackManager.getConnection().disconnect();
        super.onDestroy();
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setSuggestions(test);
            }

            @Override
            public void onSearchViewClosed() {

            }
        });
    }

    private void setup() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFragmentManager = getSupportFragmentManager();
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                BaseFragment fragment = FragmentFactory.getFragmentById(checkedId);
                mFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
        rgMain.check(R.id.rb_select);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        image = ButterKnife.findById(header, R.id.iv_drawer_image);
        tvName = ButterKnife.findById(header, R.id.tv_drawer_name);
        tvsigniture = ButterKnife.findById(header, R.id.tv_drawer_signiture);

        VCard card = SmackManager.getVCard();
        Glide.with(this).load(card.getAvatar())
                .transform(new GlideRoundTransform(this))
                .into(image);
        tvName.setText(card.getNickName());
        tvsigniture.setText(card.getField(Constant.VCARD_SIGNITURE_FIELD));
//        new LoadImageAsycTask().execute();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
                break;
            case R.id.action_message:
                Intent intent1 = new Intent(this, MessageActivity.class);
                startActivity(intent1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class LoadImageAsycTask extends AsyncTask<Void, Void, VCard> {

        @Override
        protected VCard doInBackground(Void... params) {
            VCard vCard = null;
            try {
                vCard = VCardManager.getInstanceFor(SmackManager.getConnection()).loadVCard();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return vCard;
        }

        @Override
        protected void onPostExecute(VCard card) {
            super.onPostExecute(card);
            Glide.with(MainActivity.this).load(card.getAvatar())
                    .transform(new GlideRoundTransform(MainActivity.this))
                    .into(image);
            tvName.setText(card.getNickName());
            tvsigniture.setText(card.getField(Constant.VCARD_SIGNITURE_FIELD));
        }
    }
}
