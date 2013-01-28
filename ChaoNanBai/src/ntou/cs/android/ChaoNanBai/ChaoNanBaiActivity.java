package ntou.cs.android.ChaoNanBai;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ntou.cs.android.ChaoNanBai.R;

import org.openintents.intents.AbstractWikitudeARIntent;
import org.openintents.intents.Wikitude3dARIntent;
import org.openintents.intents.WikitudeARIntent;
import org.openintents.intents.WikitudePOI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;

/**
 * This sample application demonstrates the basic usage of the Wikitude Open AR (augmented reality) API.<br/>
 * The basic usage is to create a list of POIs (points of interest) that is sent to the AR application through intent
 * extras.<br/>
 * Optional extras can be set to customize the icons or the title bar.
 * 
 * @author Martin Lechner, Mobilizy GmbH., martin.lechner@mobilizy.com
 */
public class ChaoNanBaiActivity extends Activity {

    /** the callback-intent after pressing any buttons */
    private static final String CALLBACK_INTENT = "wikitudeapi.mycallbackactivity";
    /** the baibai-intent after pressing any buttons */
    private static final String BAIBAI_INTENT = "wikitudeapi.baibaiactivity";
    /** the id of the dialog which will be created when the modelfile cannot be located */
    private static final int DIALOG_NO_MODEL_FILE_ON_SD = 1;
    /** the model ile name */
    private static final String modelFileName = Environment.getExternalStorageDirectory() + "/ateneav.obj";
    /** the latitude of the origin (0/0/0) of the model */

	private String AppName = "ChaoNanBaiApplication";
	private String AppKey = "925ba8cf-c37d-430b-bb70-b3307292b9cd";
	private String DevName = "Ricky";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.startARView();
        //this.start3dARView();
        
    }

    void startARView() {

        // Create the basic intent
        WikitudeARIntent intent = prepareIntent();

        // Optionally add a title
        intent.addTitleText("AR app with custom icons");
        intent.setPrintMarkerSubText(false);

        // Optionally: Add icons
        addIcons(intent);
        
        // Optionally: Put a custom graphical title:
        intent.addTitleImageResource(this.getResources().getResourceName(R.drawable.custom_title_bar));

        // And launch the intent
        try {
            intent.startIntent(this);
        } catch (ActivityNotFoundException e) {
            AbstractWikitudeARIntent.handleWikitudeNotFound(this);
        }
    }

    /**
     * starts the 3D AR view
     */
    private void start3dARView() {

        // Create the basic intent
        Wikitude3dARIntent intent = new Wikitude3dARIntent(this.getApplication(), AppKey, DevName);

        intent.addTitleText("Pots");
        
        File file = new File(modelFileName);
        if (!file.exists()) {
            this.showDialog(ChaoNanBaiActivity.DIALOG_NO_MODEL_FILE_ON_SD);
        } else {
            intent.setModelPathFileSystem(modelFileName);
            intent.setModelOrigin(25.14915f, 121.77474f, 0.0f);
            intent.setMetersWorth1000Points(10);

            // And launch the intent
            try {
                intent.startIntent(this);
            } catch (ActivityNotFoundException e) {
                AbstractWikitudeARIntent.handleWikitudeNotFound(this);
            }
        }
    }

    /**
     * prepares a Wikitude AR Intent (e.g. adds the POIs to the view)
     * 
     * @return the prepared intent
     */
    private WikitudeARIntent prepareIntent() {

        // create the intent
        WikitudeARIntent intent = new WikitudeARIntent(this.getApplication(), AppName, AppKey, DevName);
        // add the POIs
        this.addPois(intent);
        // add one menu item
        intent.setMenuItem1("My menu item", ChaoNanBaiActivity.CALLBACK_INTENT);
        intent.setPrintMarkerSubText(true);
        return intent;
    }

    /**
     * adds hard-coded POIs to the intent
     * 
     * @param intent
     *            the intent
     */
    private void addPois(WikitudeARIntent intent) {
        WikitudePOI poi1 = new WikitudePOI(25.03557, 121.50015, 0, "艋舺 龍山寺 觀世音菩薩",
        		"觀音佛祖，俗稱「觀音媽」，以普救世人的大慈大悲菩薩，關懷一切眾生悲慘的聲音，故又稱「觀世音」，簡稱「觀音」。");
        poi1.setLink("http://www.google.com/");
        poi1.setDetailAction(ChaoNanBaiActivity.BAIBAI_INTENT);
        WikitudePOI poi2 = new WikitudePOI(25.06307, 121.53385, 0, "中山區 行天宮 關聖帝君",
                "三國蜀漢人，關羽字雲長，一生忠義勇武，集「仁、義、禮、智、信」之大成者，歷代君王均加以封號，祭祀並以「武聖」最高榮譽尊稱。");
        poi2.setDetailAction(ChaoNanBaiActivity.BAIBAI_INTENT);
        WikitudePOI poi3 = new WikitudePOI(22.99055, 120.20428, 0, "台南 孔廟 至聖先師孔子",
                "孔子集華夏上古文化之大成，在世時已被譽為「天縱之聖」、「天之木鐸」，是當時社會上最博學者之一，並且被後世統治者尊為孔聖人、至聖、至聖先師、萬世師表。");
        poi3.setDetailAction(ChaoNanBaiActivity.BAIBAI_INTENT);
        WikitudePOI poi4 = new WikitudePOI(25.14915, 121.77474, 0, "基隆 海洋大學 夢泉",
                "海大生日派對據點、海大美麗景點之美名。");
        poi4.setDetailAction(ChaoNanBaiActivity.BAIBAI_INTENT);
        List<WikitudePOI> pois = new ArrayList<WikitudePOI>();

        pois.add(poi1);
        pois.add(poi2);
        pois.add(poi3);
        pois.add(poi4);
        intent.addPOIs(pois);

        ((ChaoNanBaiApplication) this.getApplication()).setPois(pois);
    }

    /**
     * helper-method to add icons to the intent.
     * 
     * @param intent
     *            the intent
     */
    private void addIcons(WikitudeARIntent intent) {
        ArrayList<WikitudePOI> pois = intent.getPOIs();

        Resources res = getResources();
        pois.get(0).setIconresource(res.getResourceName(R.drawable.guanyin_buddha));
        pois.get(1).setIconresource(res.getResourceName(R.drawable.kuan));
        pois.get(2).setIconresource(res.getResourceName(R.drawable.confucius));
        pois.get(3).setIconresource(res.getResourceName(R.drawable.dream));
        // to use this, make sure you have the file present on the sdcard
        // pois.get(3).setIconuri("content://com.IconCP/sdcard/flag_austria.png");
    }

    /**
     * {@inheritDoc}
     */
    protected Dialog onCreateDialog(int dialogId) {
        String message = this.getString(R.string.modelfile_not_found, modelFileName);
        return new AlertDialog.Builder(this).setTitle(R.string.modelfile_not_found_title).setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do nothing, just dismiss the dialog
                    }
                }).create();
    }
}
