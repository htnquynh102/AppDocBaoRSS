package com.example.appdocbao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<Docbao> mangdocbao;

    Customadapter customadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mangdocbao= new ArrayList<Docbao>();
        listView=(ListView) findViewById(R.id.listviewTieuDe);



        new ReadRSS().execute("https://vnexpress.net/rss/so-hoa.rss");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(MainActivity.this, NewsActivity.class);
                intent.putExtra("linkTinTuc", mangdocbao.get(position).link);
                startActivity(intent);
            }
        });
    }

    private  class ReadRSS extends AsyncTask<String, Void, String>{
        StringBuilder content= new StringBuilder();
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url= new URL(strings[0]);
                InputStreamReader inputStreamReader= new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader= new BufferedReader(inputStreamReader);

                String line="";
                while ((line= bufferedReader.readLine()) !=null){
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser= new XMLDOMParser();
            Document document=parser.getDocument(s);

            NodeList nodeList= document.getElementsByTagName("item");
            NodeList nodeListdescription = document.getElementsByTagName("description");

            String tieuDe="";
            String link="";
            String hinhanh="";

            for (int i=0; i<nodeList.getLength(); i++){
                String cdata= nodeListdescription.item(i+1).getTextContent();
                Pattern p= Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher=p.matcher(cdata);
                if(matcher.find()){
                    hinhanh= matcher.group(1);

                }
                Element element= (Element) nodeList.item(i);
                tieuDe = parser.getValue(element,"title");
                link=parser.getValue(element, "link");
                mangdocbao.add(new Docbao(tieuDe,link,hinhanh));


            }
            customadapter= new Customadapter(MainActivity.this, android.R.layout.simple_list_item_1,mangdocbao);
            listView.setAdapter(customadapter);

        }
    }


}