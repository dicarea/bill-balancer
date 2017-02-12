package es.humarbean.gespagos.fragments.mateList;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.application.GesPagosApp;
import es.humarbean.gespagos.models.Mate;

public class MateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Mate mMate;

    private MateListHelper helper;
    private MateListViewHelper helperView;

    private RelativeLayout mRelativeLayout;
    private ImageButton mImageButton;
    private TextView mMateName;
    private TextView mMateMulti;
    private TextView mMateDesc;

    public MateHolder(final View itemView, final Fragment fragment, final RecyclerView recyclerView) {
        super(itemView);

        /* Dependency Injection. */
        helper = new MateListHelper();
        helperView = new MateListViewHelper(fragment.getActivity());

//        mMateButton = (ToggleButton) itemView.findViewById(R.id.list_item_mate_button_mate);
//        mMateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                helper.markMateSelection(mMate.getId(), checked);
//                if (!recyclerView.isComputingLayout()) {
//                    //recyclerView.getAdapter().notifyItemChanged(helper.getMatePositionById(mMateId));
//                    helper.loadMateStats();
//                    recyclerView.getAdapter().notifyDataSetChanged();
//                }
//            }
//        });
//        mMateButton.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Intent intent = MateEditorActivity.newIntentEdit(fragment.getActivity(), mMate.getId());
//                fragment.startActivityForResult(intent, 44);
//                return true;
//            }
//        });
//
//        mPaymentButton = (Button) itemView.findViewById(R.id.list_item_mate_button_payment);
//        mPaymentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.markMateAsPayer(mMate.getId());
//                helper.insertRoundAndPayments(mMate.getIdGroup());
//                helperView.openToast(R.string.process_payment);
//                recyclerView.getAdapter().notifyDataSetChanged();
//            }
//        });
//
//        mMultiplierButton = (ToggleButton) itemView.findViewById(R.id.list_item_mate_button_multiplier);
//        mMultiplierButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                helper.markMultiplier(mMate.getId(), checked);
//            }
//        });
//        mMultiplierButton.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                helper.incrementMultiplier(mMate.getId());
//                recyclerView.getAdapter().notifyItemChanged(helper.getMatePositionById(mMate.getId()));
//                return true;
//            }
//        });

        mImageButton = (ImageButton) itemView.findViewById(R.id.mate_item_payment);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.markMateAsPayer(mMate.getId());
                helper.insertRoundAndPayments(mMate.getIdGroup());
                Snackbar.make(v, R.string.process_payment, Snackbar.LENGTH_LONG).show();
                recyclerView.getAdapter().notifyDataSetChanged();
                Animation animRotate = AnimationUtils.loadAnimation(GesPagosApp.getAppContext(), R.anim.rotate);
                mImageButton.startAnimation(animRotate);
            }
        });

        mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.mate_item);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selectedNow = !v.isSelected();
                mRelativeLayout.setSelected(selectedNow);
                mImageButton.setSelected(selectedNow);
                mImageButton.setEnabled(!selectedNow);
                helper.markMateSelection(mMate.getId(), selectedNow);

                if (!recyclerView.isComputingLayout()) {
                    helper.loadMateStats();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        mMateName = (TextView) itemView.findViewById(R.id.mate_item_name);
        mMateMulti = (TextView) itemView.findViewById(R.id.mate_item_multi);
        mMateDesc = (TextView) itemView.findViewById(R.id.mate_item_details);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void bindMate(Mate mate) {
        mMate = mate;

        mMateName.setText(mate.getName());
        mRelativeLayout.setSelected(mate.isSelected());

        String str = String.format("Payed %s and consumed %s",
                new Object[]{mate.getStats().getNumPayments(), mate.getStats().getNumTaken()});
        mMateDesc.setText(str);

        mMateMulti.setText(String.valueOf(mate.getStats().getWeight()));

        mImageButton.setEnabled(mate.isSelected());

        GradientDrawable background = (GradientDrawable) mImageButton.getBackground();
        if (mate.isSelected()) {
            mImageButton.setEnabled(true);
            int color = helperView.getColor(mate.getStats().getColor());
            mMateMulti.setTextColor(color);

            background.setColor(color);
        } else {
            mImageButton.setEnabled(false);
            background.setColor(ContextCompat.getColor(GesPagosApp.getAppContext(), R.color.lightgray));
        }

//        mMultiplierButton.setEnabled(mate.isSelected());
//        mMultiplierButton.setChecked(mate.isMultiplierActive());
//        mMultiplierButton.setText("X" + mate.getMultiplier());
//        mMultiplierButton.setTextOn("X" + mate.getMultiplier());
//        mMultiplierButton.setTextOff("X" + mate.getMultiplier());

    }

    @Override
    public void onClick(View v) {
        // EMPTY
    }
}
