package de.lessvoid.nifty.loaderv2.types;

import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.resolver.parameter.ParameterResolver;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.xpp3.Attributes;

public class EffectType extends XmlBaseType {
  private Logger logger = Logger.getLogger(EffectType.class.getName());

  private static final boolean DEFAULT_INHERIT = false;
  private static final boolean DEFAULT_POST = false;
  private static final boolean DEFAULT_OVERLAY = false;

  public void materialize(
      final Nifty nifty,
      final ScreenController screenController,
      final Element element,
      final EffectEventId effectEventId,
      final Attributes effectsTypeAttibutes,
      final ParameterResolver parameterResolver) {
    Attributes effectAttributes = new Attributes(getAttributes());
    effectAttributes.merge(effectsTypeAttibutes);

    Attributes attributes = parameterResolver.resolve(effectAttributes);

    RegisterEffectType registerEffectType = getRegisteredEffectType(nifty, attributes);
    if (registerEffectType == null) {
      return;
    }

    Class < ? > effectClass = registerEffectType.getEffectClass();
    if (effectClass == null) {
      return;
    }

    Effect effect = createEffect(nifty, effectEventId, attributes);
    effect.init(
        element,
        createEffectImpl(effectClass),
        attributes.createProperties(),
        nifty.getTimeProvider(),
        screenController);
    element.registerEffect(effectEventId, effect);
  }

  private RegisterEffectType getRegisteredEffectType(final Nifty nifty, final Attributes attributes) {
    String name = getEffectName(attributes);
    RegisterEffectType registerEffectType = nifty.resolveRegisteredEffect(name);
    if (registerEffectType == null) {
      logger.warning("unable to convert effect [" + name + "] because no effect with this name has been registered.");
      return null;
    }
    return registerEffectType;
  }

  private Effect createEffect(final Nifty nifty, final EffectEventId effectEventId, final Attributes attributes) {
    Effect effect = new Effect(
        nifty,
        getInherit(attributes),
        getPost(attributes),
        getOverlay(attributes),
        getAlternateKey(attributes),
        getAlternateEnable(attributes),
        effectEventId);
    initializeEffect(effect, effectEventId);
    return effect;
  }

  private boolean getInherit(final Attributes attributes) {
    return attributes.getAsBoolean("inherit", DEFAULT_INHERIT);
  }

  private boolean getPost(final Attributes attributes) {
    return attributes.getAsBoolean("post", DEFAULT_POST);
  }

  private boolean getOverlay(final Attributes attributes) {
    return attributes.getAsBoolean("overlay", DEFAULT_OVERLAY);
  }

  private boolean getAlternateEnable(final Attributes attributes) {
    String alternateEnable = attributes.get("alternateEnable");
    if (alternateEnable == null) {
      return false;
    }
    return true;
  }

  private String getAlternateKey(final Attributes attributes) {
    return attributes.get("alternateEnable");
  }

  protected void initializeEffect(final Effect effect, final EffectEventId effectEventId) {
    if (EffectEventId.onFocus.equals(effectEventId)) {
      effect.enableInfinite();
    }
  }

  private EffectImpl createEffectImpl(final Class < ? > effectClass) {
    try {
      if (EffectImpl.class.isAssignableFrom(effectClass)) {
        return (EffectImpl) effectClass.newInstance();
      } else {
        logger.warning("given effect class ["
            + effectClass.getName()
            + "] does not implement ["
            + EffectImpl.class.getName() + "]");
      }
    } catch (Exception e) {
      logger.warning("class [" + effectClass.getName() + "] could not be instanziated");
    }
    return null;
  }

  private String getEffectName(final Attributes attributes) {
    return attributes.get("name");
  }
}
