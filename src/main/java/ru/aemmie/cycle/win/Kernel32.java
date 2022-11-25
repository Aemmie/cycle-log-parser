package ru.aemmie.cycle.win;


import com.sun.jna.Native;

public interface Kernel32 extends com.sun.jna.platform.win32.Kernel32 {

    Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

    /**
     * Generates simple tones on the speaker. The function is synchronous;
     * it performs an alertable wait and does not return control to its caller until the sound finishes.
     *
     * @param dwFreq : The frequency of the sound, in hertz. This parameter must be in the range 37 through 32,767 (0x25 through 0x7FFF).
     * @param dwDuration : The duration of the sound, in milliseconds.
     */
    void Beep(int dwFreq, int dwDuration);
}
