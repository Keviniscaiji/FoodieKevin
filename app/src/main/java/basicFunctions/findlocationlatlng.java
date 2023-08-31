package basicFunctions;

import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.foodiekevin.MapActivity;

public class findlocationlatlng {

    public static LatLng getlatlog(String city,String keyword){

        SuggestionSearch mSuggestionSearch = SuggestionSearch.newInstance();
        final LatLng[] ll = {null};
        OnGetSuggestionResultListener suglistener = new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                try {
                    ll[0] = suggestionResult.getAllSuggestions().get(0).pt;
                    Log.e("TAG", suggestionResult.getAllSuggestions().get(0).pt.latitude+"");
                    Log.e("TAG", suggestionResult.getAllSuggestions().get(0).pt.longitude+"");
                } catch (NullPointerException e) {

                }

            }
        };

        mSuggestionSearch.setOnGetSuggestionResultListener(suglistener);
        mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                .city(city)
                .keyword(keyword));
        mSuggestionSearch.destroy();
        return ll[0];
    }
}
