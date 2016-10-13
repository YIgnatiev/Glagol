package net.pixeltk.glagol.api;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by o_cure on 1/23/16.
 */
public class Audio {




    @SerializedName("question") String question;
    @SerializedName("answers") String answers;


  /* public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
*/

    //-------------------------Картинки для DeteilInfo-----------------------------------//

    public class StudioData {

        @SerializedName("id") String id;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        @SerializedName("name") String name;
        public String getName(){
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        @SerializedName("lastname") String lastname;
        public String getLastname(){
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
        @SerializedName("image") String imageURL;
        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }
        @SerializedName("middlename") String middlename;
        public String getMiddlename() {
            return middlename;
        }

        public void setMiddlename(String middlename) {
            this.middlename = middlename;
        }

    }

    @SerializedName("data") ArrayList<StudioData> audiodata;
    public ArrayList<StudioData> getaudiodata() {
        return audiodata;
    }

    public void setaudiodata(ArrayList<StudioData>audiodataa) {
        this.audiodata = audiodata;
    }


}