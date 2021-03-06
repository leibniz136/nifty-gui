package de.lessvoid.nifty.slick2d.sound.music;

import de.lessvoid.nifty.slick2d.loaders.SlickLoadException;

/**
 * This exception is expected to be thrown be the constructor of this class in case loading the specified music failed.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SlickLoadMusicException extends SlickLoadException {
  /**
   * The serialization UID.
   */
  private static final long serialVersionUID = 4068721034678365719L;

  /**
   * Create the exception without an attached message or parent Throwable.
   */
  public SlickLoadMusicException() {
  }

  /**
   * Create the exception with an attached message and without parent Throwable.
   *
   * @param msg The message that is attached to this exception
   */
  public SlickLoadMusicException(final String msg) {
    super(msg);
  }

  /**
   * Create the exception with an attached message and parent Throwable.
   *
   * @param msg The message that is attached to this exception
   * @param e   The throwable that caused this exception
   */
  public SlickLoadMusicException(final String msg, final Throwable e) {
    super(msg, e);
  }

  /**
   * Create the exception without an attached message and with parent Throwable.
   *
   * @param e The throwable that caused this exception
   */
  public SlickLoadMusicException(final Throwable e) {
    super(e);
  }
}