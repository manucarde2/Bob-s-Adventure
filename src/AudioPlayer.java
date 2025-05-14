import javax.sound.sampled.*;

public class AudioPlayer
{
    private Clip clip;
    private int volume = 100;

    public AudioPlayer(String s)
    {
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    getClass().getResourceAsStream(s)
            );

            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
            clip = AudioSystem.getClip();
            clip.open(dais);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void play()
    {
        if(clip == null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void playLoop()
    {
        if(clip == null) return;
        stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop()
    {
        if(clip.isRunning()) clip.stop();
    }

    public void close()
    {
        stop();
        clip.close();
    }

    public void setVolume(int volumePercent) {
        volumePercent = Math.max(0, Math.min(100, volumePercent));
        this.volume = volumePercent;

        if (clip == null) return;

        if (clip.isControlSupported(FloatControl.Type.VOLUME)) {
            FloatControl volControl =
                    (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            float v = volumePercent / 100f;
            volControl.setValue(v);
            return;
        }
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float minDb = gainControl.getMinimum();
            float maxDb = gainControl.getMaximum();
            float db = (maxDb - minDb) * (volumePercent / 100f) + minDb;

            gainControl.setValue(db);
        }
    }
}
