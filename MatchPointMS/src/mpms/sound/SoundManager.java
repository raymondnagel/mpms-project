/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mpms.sound;

import java.util.ArrayList;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 *
 * @author rnagel
 */
public class SoundManager implements LineListener
{
    private ArrayList<Sound>   mySounds = null;
    private int             myMaxSimultaneousSounds = 0;

    public SoundManager(int maxSounds)
    {
        mySounds = new ArrayList<>(maxSounds);
        myMaxSimultaneousSounds = maxSounds;
    }

    @Override
    public void update(LineEvent event)
    {
        if (event.getType() == LineEvent.Type.STOP)
        {
            for (int s = 0; s < mySounds.size(); s++)
            {
                if (mySounds.get(s).getClip() == event.getLine())
                {
                    mySounds.remove(s);
                }
            }
        }
    }

    public boolean playSound(Sound sound)
    {
        if (mySounds.size() < myMaxSimultaneousSounds)
        {
            sound.getClip().addLineListener(this);
            mySounds.add(sound);
            sound.start();
            return true;
        }
        return false;
    }


    public boolean loopSound(Sound sound, int count)
    {
        if (mySounds.size() < myMaxSimultaneousSounds)
        {
            sound.getClip().addLineListener(this);
            mySounds.add(sound);
            sound.loop(count);
            return true;
        }
        return false;
    }
}
