package es.humarbean.gespagos.fragments.mateList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.views.MateEditorActivity;

public class MateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Integer mMateId;
    private ToggleButton mMateButton;
    private Button mPaymentButton;
    private ToggleButton mMultiplierButton;

    private MateListHelper helper;
    private MateListViewHelper helperView;

    public MateHolder(View itemView, final Activity activity, final RecyclerView recyclerView) {
        super(itemView);

        /* Dependency Injection. */
        helper = new MateListHelper();
        helperView = new MateListViewHelper(activity);

        mMateButton = (ToggleButton) itemView.findViewById(R.id.list_item_mate_button_mate);
        mMateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                helper.markMateSelection(mMateId, checked);
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
                Intent intent = MateEditorActivity.newIntentEdit(activity, mMateId);
                activity.startActivity(intent);
                return true;
            }
        });

        mPaymentButton = (Button) itemView.findViewById(R.id.list_item_mate_button_payment);
        mPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.markMateAsPayer(mMateId);
                helper.insertRoundAndPayments();
                helperView.openToast(R.string.process_payment);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        mMultiplierButton = (ToggleButton) itemView.findViewById(R.id.list_item_mate_button_multiplier);
        mMultiplierButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                helper.markMultiplier(mMateId, checked);
            }
        });
        mMultiplierButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                helper.incrementMultiplier(mMateId);
                recyclerView.getAdapter().notifyItemChanged(helper.getMatePositionById(mMateId));
                return true;
            }
        });

    }

    public void bindMate(Mate mate) {
        mMateId = mate.getId();

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
