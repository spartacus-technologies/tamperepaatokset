package tampere_paatokset.spartacus.com.tamperepaatokset;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import tampere_paatokset.spartacus.com.tamperepaatokset.data_access.DataAccess;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link testFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link testFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class testFragment extends Fragment implements DataAccess.NetworkListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinnerOrganization;

    private View view_;
    private WebView webView_;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment testFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static testFragment newInstance() {
        testFragment fragment = new testFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public testFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view_ = inflater.inflate(R.layout.fragment_test, container, false);
        view_.findViewById(R.id.buttonSearchSessions).setOnClickListener(this);
        ((Spinner)view_.findViewById(R.id.spinnerSelectOrganization)).setOnItemSelectedListener(this);
        webView_ = (WebView)view_.findViewById(R.id.webView);

        webView_.getSettings().setJavaScriptEnabled(true);
        webView_.getSettings().setSupportZoom(true);
        webView_.getSettings().setBuiltInZoomControls(true);
        URLHandler handler = new URLHandler();
        handler.netWorkListener_ = this;
        webView_.setWebViewClient(handler);

        return view_;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String httpAdder(String text){

        text = text.replace("href=\"/", "href=\"http://ktweb.tampere.fi/");
        text = text.replace("<a class=\"copyright\" href=\"http://www.triplan.fi\" target=\"_blank\">&copy; Triplan Oy</a>", "");

        return text;
    }

    @Override
    public void DataAvailable(String data, RequestType type){

        try {

            // Create the encoder and decoder for Win1252
            Charset charsetInput = Charset.forName("windows-1252");
            CharsetDecoder decoder = charsetInput.newDecoder();

            String outputEncoding = "UTF-8";
            Charset charsetOutput = Charset.forName(outputEncoding);
            CharsetEncoder encoder = charsetOutput.newEncoder();

// Convert the byte array from starting inputEncoding into UCS2
            byte[] bufferToConvert = data.getBytes();
            CharBuffer cbuf = decoder.decode(ByteBuffer.wrap(bufferToConvert));

// Convert the internal UCS2 representation into outputEncoding
            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(cbuf));
            data = new String(bbuf.array(), 0, bbuf.limit(), charsetOutput);
        } catch(Exception e) {

        }

        if (data.indexOf("%PDF") != -1) {
            return;
        }

        if (data != null) {
            Log.i("testFragment", data);

            String result = "";
            try {
                String removeBefore = "<div id=\"content\" >";
                result = data.substring(data.lastIndexOf(removeBefore), data.length());

            } catch (Exception e) {
                result = data;
            }

            result = httpAdder(result);
            Log.i("testFragment", result);
            ((WebView)getActivity().findViewById(R.id.webView)).loadData(result, "text/html; charset=UTF-8", null);
        }
    }

    @Override
    public void BinaryDataAvailable(Object data, RequestType type) {

    }

    @Override
    public void onClick(View v) {

        String date1 = ((TextView) view_.findViewById(R.id.textInputDate1)).getText().toString();
        String date2 = ((TextView) view_.findViewById(R.id.textInputDate2)).getText().toString();
        String organization = ((Spinner) view_.findViewById(R.id.spinnerSelectOrganization)).getSelectedItem().toString();

        searchData(date1, date2, organization);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String date1 = ((TextView) view_.findViewById(R.id.textInputDate1)).getText().toString();
        String date2 = ((TextView) view_.findViewById(R.id.textInputDate2)).getText().toString();
        String organization = ((Spinner) view_.findViewById(R.id.spinnerSelectOrganization)).getSelectedItem().toString();

        searchData(date1, date2, organization);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        String date1 = ((TextView) view_.findViewById(R.id.textInputDate1)).getText().toString();
        String date2 = ((TextView) view_.findViewById(R.id.textInputDate2)).getText().toString();
        String organization = ((Spinner) view_.findViewById(R.id.spinnerSelectOrganization)).getSelectedItem().toString();

        searchData(date1, date2, organization);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void searchData(String date1, String date2, String organization) {
        int start = -1;
        int end = -1;

        try {
            start = organization.lastIndexOf("(");
        } catch (Exception e){
            start = -1;
        }

        try {
            end = organization.lastIndexOf(")");
        } catch (Exception e){
            end = -1;
        }

        String param_organization = "";
        if (start != -1 && end != -1) {
            param_organization = organization.substring(start+1, end);
        }

        DataAccess.requestMeetingData(this, date1, date2, param_organization);
    }

}
