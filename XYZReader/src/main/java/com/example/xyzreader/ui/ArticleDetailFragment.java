package com.example.xyzreader.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ArticleDetailFragment";

    public static final String ARG_ITEM_ID = "item_id";
    private static final float PARALLAX_FACTOR = 1.25f;

    private Cursor mCursor;
    private long mItemId;
    private View mRootView;
    private int mMutedColor = 0xFF333333;

    @BindView(R.id.article_byline)
    TextView bylineView;

    @BindView(R.id.article_title)
    TextView titleView;
    private Unbinder unbinder;

    @BindView(R.id.photo)
    ImageView mPhotoView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }


        setHasOptionsMenu(true);
    }

    public ArticleDetailActivity getActivityCast() {
        return (ArticleDetailActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(0, null, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        mPhotoView = (ImageView) mRootView.findViewById(R.id.photo);
        mRootView.findViewById(R.id.share_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Get your ( " + mCursor.getString(ArticleLoader.Query.TITLE) + " ) From our XYZ Reader")
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        bindViews();
        //  updateStatusBar();
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void bindViews() {
        if (mRootView == null) {
            return;
        }

        titleView = (TextView) mRootView.findViewById(R.id.article_title);
        bylineView = (TextView) mRootView.findViewById(R.id.article_byline);


        RecyclerView recyclerView = mRootView.findViewById(R.id.article_data_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (mCursor != null) {
            //  mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            //   mRootView.animate().alpha(1);

            titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
            bylineView.setText(mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE));


            Picasso.with(getContext()).load(mCursor.getString(ArticleLoader.Query.PHOTO_URL)).into(mPhotoView);

            //
            ArticleDetailTextAdapter articleDetailTextAdapter = new ArticleDetailTextAdapter(getContext(), mCursor.getString(ArticleLoader.Query.BODY));
            recyclerView.setAdapter(articleDetailTextAdapter);


        } else {
            mRootView.setVisibility(View.VISIBLE);

            Toast.makeText(this.getActivity(), "dsf", Toast.LENGTH_SHORT).show();
            //  titleView.setText("N/A");
            //  bylineView.setText("N/A" );
            //bodyView.setText("N/A");
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!isAdded()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        mCursor = cursor;
        if (mCursor != null && !mCursor.moveToFirst()) {
            Log.e(TAG, "Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bindViews();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        //   mCursor = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bindViews();
        }
    }


}
