package ir.doorbash.hexy.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import ir.doorbash.hexy.R;
import ir.doorbash.hexy.fragments.HowToPlayImageFragment;

public class HowToPlayDialog extends DialogFragment {

    Runnable listener;

    ViewPager viewPager;
    AppCompatImageView next;
    AppCompatImageView prev;
    AppCompatImageView close;

    public static HowToPlayDialog newInstance(Runnable listener) {
        HowToPlayDialog fragment = new HowToPlayDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.listener = listener;
        return fragment;
    }

    public static void showDialog(AppCompatActivity activity, Runnable onDismissed) {
        FragmentManager fm = activity.getSupportFragmentManager();
        HowToPlayDialog myDialogFragment = HowToPlayDialog.newInstance(onDismissed);
        myDialogFragment.show(fm, "dialog");
    }

    @Override
    public void onResume() {
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(lp);
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_how, container, false);

        viewPager = contentView.findViewById(R.id.viewpager);
        next = contentView.findViewById(R.id.next);
        prev = contentView.findViewById(R.id.prev);
        close = contentView.findViewById(R.id.close);

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) prev.setVisibility(View.INVISIBLE);
                else prev.setVisibility(View.VISIBLE);
                if (i == 2) next.setVisibility(View.INVISIBLE);
                else next.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        viewPager.post(() -> {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewPager.getLayoutParams();
            layoutParams.width = (int) (screenWidth * 0.8f);
            layoutParams.height = (int) (screenHeight * 0.8f);
            viewPager.setLayoutParams(layoutParams);
            prev.setVisibility(View.INVISIBLE);
        });

        next.setOnClickListener(view -> {
            int nextItem = viewPager.getCurrentItem() + 1;
            if (nextItem > 2) return;
            viewPager.setCurrentItem(nextItem, true);
        });

        prev.setOnClickListener(view -> {
            int prevItem = viewPager.getCurrentItem() - 1;
            if (prevItem < 0) return;
            viewPager.setCurrentItem(prevItem, true);
        });

        close.setOnClickListener(view -> {
            dismiss();
        });

        return contentView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) listener.run();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        String[] titles;
        Fragment[] fragments = new Fragment[3];

        PagerAdapter(FragmentManager fm) {
            super(fm);
            fragments[0] = HowToPlayImageFragment.newInstance(R.drawable.ui_h_1);
            fragments[1] = HowToPlayImageFragment.newInstance(R.drawable.ui_h_2);
            fragments[2] = HowToPlayImageFragment.newInstance(R.drawable.ui_h_3);
        }

        @Override
        public Fragment getItem(int num) {
            return fragments[num];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        void setTitles(String[] titles) {
            this.titles = titles;
        }
    }
}