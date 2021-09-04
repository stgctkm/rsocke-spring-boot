package rsockrt.model;

public class MovieScene {
    int sceneId;
    String sceneDescription;

    public MovieScene() {
    }

    public MovieScene(int sceneId, String sceneDescription) {
        this.sceneId = sceneId;
        this.sceneDescription = sceneDescription;
    }

    public String sceneDescription() {
        return sceneDescription;
    }

    public int sceneId() {
        return sceneId;
    }


    // TODO なくしたい
    public int getSceneId() {
        return sceneId;
    }

    // TODO なくしたい
    public String getSceneDescription() {
        return sceneDescription;
    }

}
