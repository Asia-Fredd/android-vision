package asia.fredd.tools.creditcardutils.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.samples.vision.ocrreader.OcrCaptureActivity;
import com.google.android.gms.samples.vision.ocrreader.R;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {

    private static final int REQUEST_CARD_SCAN = "REQUEST_CARD_SCAN".hashCode() & 0xFF;

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.card_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //noinspection ConstantConditions
                    getActivity().getCurrentFocus().clearFocus();
                } catch (Throwable e) {
                    /* nothing-to-do */
                }
                startActivityForResult(
                        new Intent(v.getContext(), OcrCaptureActivity.class)
                , REQUEST_CARD_SCAN);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CARD_SCAN && resultCode == RESULT_OK && data != null) {
            CharSequence card_number = data.getCharSequenceExtra("card_number");
            CharSequence card_date   = data.getCharSequenceExtra("card_date");
            ((TextView) getView().findViewById(R.id.card_number)).setText(card_number);
            ((TextView) getView().findViewById(R.id.card_date))  .setText(card_date);
        }
    }
}
