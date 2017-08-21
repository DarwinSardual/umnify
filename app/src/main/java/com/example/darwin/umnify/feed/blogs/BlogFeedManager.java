package com.example.darwin.umnify.feed.blogs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.darwin.umnify.R;
//import com.example.darwin.umnify.connection.RemoteDbConn;
import com.example.darwin.umnify.authentication.AuthenticationAddress;
import com.example.darwin.umnify.authentication.AuthenticationKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class BlogFeedManager extends RecyclerView.Adapter<BlogFeedManager.ViewHolder>{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private List<BlogTile> feedList;
    private BlogFeedAsync blogHandler;

    private boolean isFetching = false;

    private String crlf = "\r\n";
    private String twoHyphens = "--";
    private String boundary = "*****";

    private Activity activity;

    public BlogFeedManager(Activity activity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView){

        this.activity = activity;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;

        feedList = new ArrayList<>();

        blogHandler = new BlogFeedAsync(AuthenticationAddress.FETCH_BLOGS);
        isFetching = true;
        blogHandler.execute("tile", "desc", feedList.size() + "", "8");

    }

    public final class ViewHolder extends RecyclerView.ViewHolder{

        private TextView blogTileHeadingView;
        private ImageView blogTileImageView;
        private RelativeLayout container;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.feed_blogs, parent, false));

            container = (RelativeLayout) itemView.findViewById(R.id.blog_tile_container);
            blogTileHeadingView = (TextView) itemView.findViewById(R.id.blog_tile_heading);
            blogTileImageView = (ImageView) itemView.findViewById(R.id.blog_tile_image);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position < feedList.size()){

            final BlogTile blogTile = feedList.get(position);
            holder.blogTileHeadingView.setText(blogTile.getHeading());
            holder.blogTileImageView.setImageBitmap(blogTile.getImage());

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BlogActivity.class);
                    intent.putExtra("BLOG_TILE_ID", blogTile.getId());
                    intent.putExtra("BLOG_TILE_HEADING", blogTile.getHeading());
                    intent.putExtra("BLOG_TILE_IMAGE","");

                    view.getContext().startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void addEntries(String data) throws JSONException{

        JSONArray dataList = new JSONArray(data);
        int temp = feedList.size();

        for(int i = 0; i < dataList.length(); i++){

            JSONObject blogData = new JSONObject(dataList.getString(i));
            BlogTile blogTile = BlogHelper.createBlogTileFromJSON(blogData, feedList.size());
            BlogHelper.fetchImage(blogTile, this, activity);
            feedList.add(blogTile);
        }
        notifyItemRangeInserted(temp, dataList.length());
        swipeRefreshLayout.setRefreshing(false);
    }

    public void updateFeed(int direction){

        if(isFetching) return;

        if(direction == 1){

            blogHandler = new BlogFeedAsync(AuthenticationAddress.FETCH_BLOGS);
            String temp = feedList.size()/2 ==0? "2":"3";
            isFetching = true;
            blogHandler.execute("tile", "desc", feedList.size() + "", temp);
        }else if(direction == -1){
            feedList.clear();
            notifyDataSetChanged();

            blogHandler = new BlogFeedAsync(AuthenticationAddress.FETCH_BLOGS);
            isFetching = true;
            blogHandler.execute("tile", "desc", feedList.size() + "", "8");

        }

    }

    private class BlogFeedAsync extends RemoteDbConn<String, Void, String>{

        public BlogFeedAsync(String urlAdress){
            super(urlAdress, BlogFeedManager.this.activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{

                setUpConnection();
                Uri.Builder queryBuilder = super.getQueryBuilder();
                queryBuilder.appendQueryParameter("type", strings[0])
                        .appendQueryParameter("order", strings[1])
                        .appendQueryParameter("offset", strings[2])
                        .appendQueryParameter("limit", strings[3]);

                super.setRequest(queryBuilder.build().getEncodedQuery());
                super.getUrlConnection().connect();

                String response = super.getRequest();

                return response;
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            //super.onPostExecute(s);

            try {
                JSONObject str = new JSONObject(response);
                String data = str.getString("data");

               // if(DIRECTION == 1)
                    addEntries(data);
                //else if(DIRECTION == -1);

                isFetching = false;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void addBlog(Intent data, Bundle userData){

        Uri uri = data.getData();
        Cursor cursor;
        DataWrapper blogWrapper = null;

        if(uri != null){

            cursor = activity.getContentResolver().query(uri,
                    null, null,
                    null, null);

            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();

            String imageFile = cursor.getString(nameIndex);
            Bitmap image = null;

            try{
                image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            }catch (IOException e){
                e.printStackTrace();
            }

            blogWrapper = new DataWrapper(data.getStringExtra("ADD_BLOG_HEADING"), data.getStringExtra("ADD_BLOG_CONTENT"),
                    imageFile, userData.getInt("USER_ID"), userData.getInt("USER_TYPE"), image);

        }else{
            blogWrapper = new DataWrapper(data.getStringExtra("ADD_BLOG_HEADING"), data.getStringExtra("ADD_BLOG_CONTENT"),
                    null, userData.getInt("USER_ID"), userData.getInt("USER_TYPE"), null);
        }

        AddBlogAsync addBlogAsync = new AddBlogAsync(AuthenticationAddress.ADD_BLOG, activity);
        addBlogAsync.execute(blogWrapper);
    }

    private class DataWrapper{

        private String heading;
        private String content;
        private String imageFile;
        private Bitmap image;
        private int userType;
        private int authorId;

        public DataWrapper(String heading, String content, String imageFile, int authorId, int userType, Bitmap image){
            this.content = content;
            this.image = image;
            this.authorId = authorId;
            this.imageFile = imageFile;
            this.userType = userType;
            this.heading = heading;
        }

        public String getContent() {
            return content;
        }

        public int getAuthorId() {
            return authorId;
        }

        public Bitmap getImage() {
            return image;
        }

        public String getImageFile() {
            return imageFile;
        }

        public int getUserType() {
            return userType;
        }

        public String getHeading() {
            return heading;
        }
    }

    private class AddBlogAsync extends RemoteDbConn<DataWrapper, Void, String>{

        public AddBlogAsync(String urlAddress, Activity activity){
            super(urlAddress, activity);
        }

        @Override
        protected String doInBackground(DataWrapper... wrapper) {


            try{

                setUpConnection();
                HttpURLConnection urlConnection = super.getUrlConnection();

                if(wrapper[0].getImageFile() != null){

                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("Cache-Control", "no-cache");
                    urlConnection.setRequestProperty(
                            "Content-Type", "multipart/form-data;boundary=" + BlogFeedManager.this.boundary);

                    OutputStream out = urlConnection.getOutputStream();
                    DataOutputStream outputStream = new DataOutputStream(out);

                    // start writing the iamge to buffer
                    outputStream.writeBytes(BlogFeedManager.this.twoHyphens + BlogFeedManager.this.boundary + BlogFeedManager.this.crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                            "image" + "\";filename=\"" +
                            wrapper[0].getImageFile() + "\"" + BlogFeedManager.this.crlf);
                    outputStream.writeBytes(BlogFeedManager.this.crlf);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    wrapper[0].getImage().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    outputStream.write(byteArray);

                    outputStream.writeBytes(BlogFeedManager.this.crlf);
                    outputStream.writeBytes(BlogFeedManager.this.twoHyphens + BlogFeedManager.this.boundary +
                            BlogFeedManager.this.twoHyphens + BlogFeedManager.this.crlf);

                    // end writing the image

                    // authentication

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\""+AuthenticationKeys.IDENTIFICATION_KEY+"\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + AuthenticationKeys.IDENTIFICATION_VALUE + crlf);
                    //outputStream.writeBytes(crlf);

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\""+AuthenticationKeys.USERNAME_KEY+"\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + AuthenticationKeys.USERNAME_VALUE+ crlf);


                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\""+AuthenticationKeys.PASSWORD_KEY+"\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + AuthenticationKeys.PASSWORD_VALUE+ crlf);
                    // end authentication

                    // text data
                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"heading\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + wrapper[0].getHeading() + crlf);

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"content\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + wrapper[0].getContent() + crlf);
                    //outputStream.writeBytes(crlf);

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"image_file\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + wrapper[0].getImageFile() + crlf);

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"user_type\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + wrapper[0].getUserType()  + crlf);

                    outputStream.writeBytes(twoHyphens+ boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"author\";" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + crlf);
                    outputStream.writeBytes(crlf + wrapper[0].getAuthorId() + crlf);

                    outputStream.flush();
                    outputStream.close();
                    out.close();

                }else{

                    Uri.Builder queryBuilder = super.getQueryBuilder();
                    queryBuilder.appendQueryParameter("content", wrapper[0].getContent())
                            .appendQueryParameter("heading", wrapper[0].getHeading())
                            .appendQueryParameter("image", wrapper[0].getImageFile())
                            .appendQueryParameter("user_type", wrapper[0].getUserType() + "")
                            .appendQueryParameter("author", wrapper[0].getAuthorId() + "");

                    super.setRequest(getQueryBuilder().build().getEncodedQuery());
                }

                urlConnection.connect();
               // String response = getRequest();
                //return  response;

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                return reader.readLine() + " " + reader.readLine();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("Response", s);
        }
    }
}