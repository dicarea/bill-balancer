package es.humarbean.gespagos.fragments.mateList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.views.MateEditorActivity;

public class MateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Mate mMate;
    private ToggleButton mMateButton;
    private Button mPaymentButton;
    private ToggleButton mMultiplierButton;

    private MateListHelper helper;
    private MateListViewHelper helperView;

    public MateHolder(final View itemView, final Fragment fragment, final RecyclerView recyclerView) {
        super(itemView);

        /* Dependency Injection. */
        helper = new MateListHelper();
        helperView = new MateListViewHelper(fragment.getActivity());

        mMateButton = (ToggleButton) itemView.findViewById(R.id.list_item_mate_button_mate);
        mMateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                helper.markMateSelection(mMate.getId(), checked);
                if (!recyclerView.isComputingLayout()) {
                    //recyclerView.getAdapter().notifyItemChanged(helper.getMatePositionById(mMateId));
                    helper.loadMateStats();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
        mMateButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = MateEditorActivity.newIntentEdit(fragment.getActivity(), mMate.getId());
                fragment.startActivityForResult(intent, 44);
                return true;
            }
        });

        mPaymentButton = (Button) itemView.findViewById(R.id.list_item_mate_button_payment);
        mPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.markMateAsPayer(mMate.getId());
                helper.insertRoundAndPayments(mMate.getIdGroup());
                helperView.openToast(R.string.process_payment);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        mMultiplierButton = (ToggleButton) itemView.findViewById(R.id.list_item_mate_button_multiplier);
        mMultiplierButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                helper.markMultiplier(mMate.getId(), checked);
            }
        });
        mMultiplierButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                helper.incrementMultiplier(mMate.getId());
                recyclerView.getAdapter().notifyItemChanged(helper.getMatePositionById(mMate.getId()));
                return true;
            }
        });

    }

    public void bindMate(Mate mate) {
        mMate = mate;

        mMateButton.setTextOn(mate.getName());
        mMateButton.setTextOff(mate.getName());
        mMateButton.setChecked(mate.isSelected());

        mPaymentButton.setText(String.valueOf(mate.getStats().getWeight()));
        mPaymentButton.setEnabled(mate.isSelected());
        if (mate.isSelected()) {
            int color = helperView.getColor(mate.getStats().getColor());
            mPaymentButton.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            mPaymentButton.setTextColor(Color.BLACK);
        } else {
            mPaymentButton.getBackground().clearColorFilter();
            mPaymentButton.setTextColor(Color.GRAY);
        }

        mMultiplierButton.setEnabled(mate.isSelected());
        mMultiplierButton.setChecked(mate.isMultiplierActive());
        mMultiplierButton.setText("X" + mate.getMultiplier());
        mMultiplierButton.setTextOn("X" + mate.getMultiplier());
        mMultiplierButton.setTextOff("X" + mate.getMultiplier());

    }

    @Override
    public void onClick(View v) {
        // EMPTY
    }
}
