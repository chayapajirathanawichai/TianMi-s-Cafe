/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javazoom.jl.player.Player;

public class SoundManager {
    private static Map<String, String> soundMap = new HashMap<>();
    private static Player bgmPlayer;
    private static Thread bgmThread;
    private static boolean isBgmLooping = false;
    
    static {
        soundMap.put("fail", "/assets/fail.mp3");
        soundMap.put("success", "/assets/success.mp3");
        soundMap.put("pop", "/assets/pop.mp3");
        soundMap.put("bonus", "/assets/bonus.mp3");
        soundMap.put("bell", "/assets/bell.mp3");
        soundMap.put("cafe_intro", "/assets/cafe_intro.mp3");
        soundMap.put("ohno", "/assets/ohno.mp3");
        soundMap.put("scream", "/assets/scream.mp3");
    }
    public static void playSound(String key) {
        String path = soundMap.get(key);
        if (path == null) return;

        new Thread(() -> {
            try {
                InputStream is = SoundManager.class.getResourceAsStream(path);
                if (is != null) {
                    Player player = new Player(is);
                    player.play();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void playBGM(String key) {
        stopBGM();
        String path = soundMap.get(key);
        if (path == null) return;

        isBgmLooping = true;
        bgmThread = new Thread(() -> {
            while (isBgmLooping) {
                try {
                    InputStream is = SoundManager.class.getResourceAsStream(path);
                    if (is != null) {
                        bgmPlayer = new Player(is);
                        bgmPlayer.play();
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }
        });
        bgmThread.start();
    }

    public static void stopBGM() {
        isBgmLooping = false;
        if (bgmPlayer != null) {
            bgmPlayer.close();
            bgmPlayer = null;
        }
        if (bgmThread != null) {
            bgmThread.interrupt();
            bgmThread = null;
        }
    }
}
