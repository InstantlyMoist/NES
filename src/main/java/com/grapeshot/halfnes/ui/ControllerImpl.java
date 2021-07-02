/*
 * HalfNES by Andrew Hoffman
 * Licensed under the GNU GPL Version 3. See LICENSE file
 */
package com.grapeshot.halfnes.ui;

import com.grapeshot.halfnes.PrefsSingleton;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.prefs.Preferences;

import static com.grapeshot.halfnes.utils.*;

/**
 *
 * @author Andrew, Zlika This class uses the JInput Java game controller API
 * (cf. http://java.net/projects/jinput).
 */
public class ControllerImpl {

    //private final java.awt.Component parent;
    private final ScheduledExecutorService thread = Executors.newSingleThreadScheduledExecutor();
    private int latchbyte = 0, controllerbyte = 0, prevbyte = 0, outbyte = 0, gamepadbyte = 0;
    private final HashMap<Integer, Integer> m = new HashMap<>(10);
    private final int controllernum;

    public ControllerImpl(final java.awt.Component parent, final int controllernum) {
        this(controllernum);
        //this.parent = parent;
    }

    public ControllerImpl(final int controllernum) {
        if ((controllernum != 0) && (controllernum != 1)) {
            throw new IllegalArgumentException("controllerNum must be 0 or 1");
        }
        this.controllernum = controllernum;
    }

    private void pressKey(int keyCode) {
        //enable the byte of whatever is found
        prevbyte = controllerbyte;
        if (!m.containsKey(keyCode)) {
            return;
        }
        //enable the corresponding bit to the key
        controllerbyte |= m.get(keyCode);
        //special case: if up and down are pressed at once, use whichever was pressed previously
        if ((controllerbyte & (BIT4 | BIT5)) == (BIT4 | BIT5)) {
            controllerbyte &= ~(BIT4 | BIT5);
            controllerbyte |= (prevbyte & ~(BIT4 | BIT5));
        }
        //same for left and right
        if ((controllerbyte & (BIT6 | BIT7)) == (BIT6 | BIT7)) {
            controllerbyte &= ~(BIT6 | BIT7);
            controllerbyte |= (prevbyte & ~(BIT6 | BIT7));
        }
    }

    public void keyReleased(final KeyEvent keyEvent) {
        releaseKey(keyEvent.getKeyCode());
    }

    private void releaseKey(int keyCode) {
        prevbyte = controllerbyte;
        if (!m.containsKey(keyCode)) {
            return;
        }
        controllerbyte &= ~m.get(keyCode);
    }

    public void strobe() {
        //shifts a byte out
        outbyte = latchbyte & 1;
        latchbyte = ((latchbyte >> 1) | 0x100);
    }

    public void output(final boolean state) {
        latchbyte = gamepadbyte | controllerbyte;
    }
}
