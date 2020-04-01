package model;

public class Voice {
    private String voiceName;
    private int voiceMinutes;

    public Voice(String voiceName, int voiceMinutes) {
        this.voiceName = voiceName;
        this.voiceMinutes = voiceMinutes;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public int getVoiceMinutes() {
        return voiceMinutes;
    }

    public boolean isNotBlank() {
        if (voiceName == null || voiceMinutes <= 0)
            return false;
        else
            return !voiceName.isBlank();
    }

}
