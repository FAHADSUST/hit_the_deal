package fragments;

import java.util.HashMap;
import java.util.Map;

import necessaryMethods.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fahad.ornob.sust.hitthedeal.FavouriteCreatorActivityOrnob;
import com.fahad.ornob.sust.hitthedeal.R;


@SuppressLint("NewApi")
public class FragmentFavourtieCreatorAbout extends Fragment {

	static TextView nameTv;
	static TextView addressTv;
	static TextView phnTv;
	static TextView emailTv;
	static TextView typeTv;
	static Button likeButton;
	static boolean isFavourite = false;
	static Activity activity;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_favourite_creator_about,
				null);
		activity = getActivity();
		nameTv = (TextView) view.findViewById(R.id.nameTv1);
		addressTv = (TextView) view.findViewById(R.id.addressTv1);
		phnTv = (TextView) view.findViewById(R.id.phnTv1);
		emailTv = (TextView) view.findViewById(R.id.emailTv1);
		typeTv = (TextView) view.findViewById(R.id.creator_typeTv1);

		likeButton = (Button) view.findViewById(R.id.like_button);
		likeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFavourite) {
					makeVolleyRequest(3);
				} else {
					makeVolleyRequest(1);
				}
			}
		});

		// updateProfieViews();

		return view;
	}

	public static void updateProfieViews() {
		nameTv.setText(FavouriteCreatorActivityOrnob.creator.getUserName());
		addressTv.setText("  " + FavouriteCreatorActivityOrnob.creator.getAddress());
		phnTv.setText("  " + FavouriteCreatorActivityOrnob.creator.getPhnNo());
		emailTv.setText("  " + FavouriteCreatorActivityOrnob.creator.getEmail());
		typeTv.setText("  " + FavouriteCreatorActivityOrnob.creator.getCreatorType()
				+ "  ");
		makeVolleyRequest(2);
	}

	public static void makeVolleyRequest(final int option) {
		StringRequest strReq = new StringRequest(Method.POST,
				constants.Constants.URL + "IGDFavourite",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null) {
							try {
								JSONArray jsonArray = new JSONArray(response);
								JSONObject successObject = jsonArray
										.getJSONObject(0);
								if (successObject.getBoolean("success") == true) {
									likeButton.setVisibility(View.VISIBLE);
									if (option == 1 || option == 2) {
										isFavourite = true;
										likeButton
												.setBackgroundResource(R.drawable.liked);
									} else if (option == 3) {
										isFavourite = false;
										likeButton
												.setBackgroundResource(R.drawable.not_liked);
									}
								} else if (successObject.getBoolean("success") == false) {
									likeButton.setVisibility(View.VISIBLE);
									isFavourite = false;
									likeButton
											.setBackgroundResource(R.drawable.not_liked);
								}

							} catch (JSONException e) {
								Methods.makeToast(activity,
										"Update Not Successful",
										Toast.LENGTH_LONG);
							}
						} else {

						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						NetworkResponse networkResponse = error.networkResponse;
						if (networkResponse == null) {
							Methods.makeToast(activity, "network null",
									Toast.LENGTH_LONG);
						}
						Methods.makeToast(activity, error.toString(),
								Toast.LENGTH_LONG);
						// Methods.makeToast(CreateEventActivity.this,
						// "Network Error", Toast.LENGTH_LONG);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();

				params.put("creator_id", Integer
						.toString(FavouriteCreatorActivityOrnob.creator.getUserId()));
				params.put("viewer_id",
						Integer.toString(FavouriteCreatorActivityOrnob.vId));
				params.put("option", Integer.toString(option));

				return params;
			}
		};

		Volley.newRequestQueue(activity).add(strReq);
	}
}
