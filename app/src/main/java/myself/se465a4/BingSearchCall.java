package myself.se465a4;

import android.os.AsyncTask;
import android.os.Message;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class BingSearchCall extends AsyncTask<Void, Void, String> {

    private Exception exception;
    String query;
    ImageCollectionModel m;

//    protected void onPreExecute() {
//        progressBar.setVisibility(View.VISIBLE);
//        responseView.setText("");
//    }

    public BingSearchCall(String query, ImageCollectionModel model){
        this.query = query;
        m = model;
    }

    protected String doInBackground(Void... urls) {
        try {
            URL url = new URL("https://api.datamarket.azure.com/Bing/Search/v1/Image?Query=%27" + URLEncoder.encode(query, "utf-8")+ "%27&$format=json");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("", "pLxyZ/LSMIniYTk1z/oa+nPusuL8aLI4cEHas21LKbY".toCharArray());
                }
            });
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        Log.i("INFO", response);
        InputStream stream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
        try{
            ArrayList<ImageModel> outputThis = (ArrayList<ImageModel>)readJsonStream(stream);
            for (int i = 0; i < outputThis.size() && i < 10; i++) {
                ImageModel imgMod  = outputThis.get(i);
                Log.d("imgMod name", imgMod.getFileName());
                m.addPicture(imgMod);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List readJsonStream(InputStream in) throws IOException {
    JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
    try {
        return readObjectD(reader);
    }
        finally {
            reader.close();
        }
    }

    public List readObjectD(JsonReader reader) throws IOException {
        reader.beginObject();
        if (reader.nextName().equals("d")) {
            return readResults(reader);
        }
        reader.endObject();
        return null;
    }

    public List readResults(JsonReader reader) throws IOException {
        reader.beginObject();
        if (reader.nextName().equals("results")) {
            return readResultsArray(reader);
        }
        reader.endObject();
        return null;
    }

    public List readResultsArray(JsonReader reader) throws IOException {
        List results = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            ImageModel parsedResult = readResult(reader);

            String fileName = parsedResult.getPathURL().toLowerCase();
            if (Utilities.isJpgFile(fileName)||
                    Utilities.isGifFile(fileName)||
                    Utilities.isPngFile(fileName)
                    ){
                results.add(parsedResult);
            }
        }
        reader.endArray();
        return results;
    }

    public ImageModel readResult(JsonReader reader) throws IOException {
        String URL = "";
        String fileName = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Title")) {
                fileName = reader.nextString();
            } else if (name.equals("MediaUrl")) {
                URL = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new ImageModel(URL, fileName);
    }


}
