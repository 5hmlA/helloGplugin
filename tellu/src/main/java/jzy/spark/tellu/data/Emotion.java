package jzy.spark.tellu.data;

import java.util.Objects;

public class Emotion {

    public String emotion;
    public String tips;
    public String jumpUri;

    public Emotion(String emotion, String tips) {
        this.emotion = emotion;
        this.tips = tips;
    }

    public Emotion(String emotion, String tips, String jumpUri) {
        this.emotion = emotion;
        this.tips = tips;
        this.jumpUri = jumpUri;
    }

    public static Emotion defEmotion(){
        return new Emotion("default", "errorerrorerrorrrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerror");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emotion emotion1 = (Emotion)o;
        return Objects.equals(emotion, emotion1.emotion) && Objects.equals(tips, emotion1.tips);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emotion, tips);
    }

    @Override
    public String toString() {
        return "Emotion{" + "emotion='" + emotion + '\'' + ", tips='" + tips + '\'' + '}';
    }
}
