package com.vaadin.recipes.recipe.absolutelayout;


import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;

/**
 * AbsoluteLayout is a layout implementation that mimics html absolute
 * positioning.
 *
 */
public class AbsoluteLayout extends Composite<Div> implements HasSize {

    private Div div = new Div();

    private final LinkedHashMap<Component, ComponentPosition> componentToCoordinates = new LinkedHashMap<>();

    /**
     * Creates an AbsoluteLayout with full size.
     */
    public AbsoluteLayout() {
        
    }

    @Override
    protected Div initContent() {
        div.getElement().getStyle().set("position", "relative");
        div.setSizeFull();
        return div;
    }

    /**
     * Gets an iterator for going through all components enclosed in the
     * absolute layout.
     */
    public Iterator<Component> iterator() {
        return Collections
                .unmodifiableCollection(componentToCoordinates.keySet())
                .iterator();
    }

    /**
     * Gets the number of contained components. Consistent with the iterator
     * returned by {@link #getComponentIterator()}.
     *
     * @return the number of contained components
     */
    public int getComponentCount() {
        return componentToCoordinates.size();
    }
 
    /**
     * Replaces one component with another one. The new component inherits the
     * old components position.
     */
    public void replaceComponent(Component oldComponent,
            Component newComponent) {
        ComponentPosition position = getPosition(oldComponent);
        removeComponent(oldComponent);
        addComponent(newComponent, position);
    }

    public void addComponent(Component component) {
        addComponent(component, new ComponentPosition());
    }

    /**
     * Adds a component to the layout. The component can be positioned by
     * providing a string formatted in CSS-format.
     * <p>
     * For example the string "top:10px;left:10px" will position the component
     * 10 pixels from the left and 10 pixels from the top. The identifiers:
     * "top","left","right" and "bottom" can be used to specify the position.
     * </p>
     *
     * @param c
     *            The component to add to the layout
     * @param cssPosition
     *            The css position string
     */
    public void addComponent(Component c, String cssPosition) {
        ComponentPosition position = new ComponentPosition();
        position.setCSSString(cssPosition);
        addComponent(c, position);
    }

    /**
     * Adds the component using the given position. Ensures the position is only
     * set if the component is added correctly.
     *
     * @param component
     *            The component to add
     * @param componentPosition
     *            The position info for the component. Must not be null.
     * @throws IllegalArgumentException
     *             If adding the component failed
     */
    private void addComponent(Component component,
            ComponentPosition componentPosition) {
        if (equals(component.getParent())) {
            div.remove(component);
        }
        internalSetPosition(component, componentPosition);
        try {
            div.add(component);
        } catch (IllegalArgumentException e) {
            internalRemoveComponent(component);
            throw e;
        }

    }
   
    private void internalRemoveComponent(Component component) {
        componentToCoordinates.remove(component);
    }

    /**
     * Updates the position for a component. Caller must ensure component is a
     * child of this layout.
     *
     * @param component
     *            The component. Must be a child for this layout. Not enforced.
     * @param position
     *            New position. Must not be null.
     */
    private void internalSetPosition(Component component,
            ComponentPosition position) {
        componentToCoordinates.put(component, position);
        updatePosition(component, position);
    }

    private void updatePosition(Component component,
            ComponentPosition position) {
        resetPosition(component);
        for (String style : position.getCSSString().split(";")) {
            String[] css = style.split(":");
            component.getElement().getStyle().set(css[0], css[1]);
        }
    }

    private void updateComponentPosition(ComponentPosition position) {
        componentToCoordinates.forEach((key, value) -> {
            if (value.equals(position)) {
                resetPosition(key);
                for (String style : value.getCSSString().split(";")) {
                    String[] css = style.split(":");
                    key.getElement().getStyle().set(css[0], css[1]);
                }               
            }
        });
    }

    private void resetPosition(Component component) {
        component.getElement().getStyle().set("position", "absolute");
        component.getElement().getStyle().remove("bottom");
        component.getElement().getStyle().remove("top");
        component.getElement().getStyle().remove("right");
        component.getElement().getStyle().remove("left");
    }

    public void removeComponent(Component c) {
        internalRemoveComponent(c);
        div.remove(c);
    }

    /**
     * Sets the position of a component in the layout.
     *
     * @param component
     * @param position
     */
    public void setPosition(Component component, ComponentPosition position) {
        if (!componentToCoordinates.containsKey(component)) {
            throw new IllegalArgumentException(
                    "Component must be a child of this layout");
        }
        internalSetPosition(component, position);
    }

    /**
     * Gets the position of a component in the layout. Returns null if component
     * is not attached to the layout.
     * <p>
     * Note that you cannot update the position by updating this object. Call
     * {@link #setPosition(Component, ComponentPosition)} with the updated
     * {@link ComponentPosition} object.
     * </p>
     *
     * @param component
     *            The component which position is needed
     * @return An instance of ComponentPosition containing the position of the
     *         component, or null if the component is not enclosed in the
     *         layout.
     */
    public ComponentPosition getPosition(Component component) {
        return componentToCoordinates.get(component);
    }

    public void setTopLeft(Component component, float top, float left) {
        ComponentPosition position = componentToCoordinates.get(component);
        if (position != null) {
            position.setTop(top, Unit.PIXELS);
            position.setLeft(left, Unit.PIXELS);
        }
    }

    public void setBottomRight(Component component, float bottom, float right) {
        ComponentPosition position = componentToCoordinates.get(component);
        if (position != null) {
            position.setBottom(bottom, Unit.PIXELS);
            position.setRight(right, Unit.PIXELS);
        }
    }

    public void addTopLeft(Component component, float top, float left) {
        addComponent(component,"top:"+top+"px;left:"+left+"px");
    }

    public void addBottomRight(Component component, float bottom, float right) {
        addComponent(component,"bottom:"+bottom+"px;right:"+right+"px");
    }

    @Deprecated
    public void requestRepaint() {
        markAsDirty();
    }

    @Deprecated
    public void markAsDirty() {
        // NOP for compatibility
    }

    /**
     * The CompontPosition class represents a components position within the
     * absolute layout. It contains the attributes for left, right, top and
     * bottom and the units used to specify them.
     */
    public class ComponentPosition implements Serializable {

        private int zIndex = -1;
        private Float topValue = 1f;
        private Float rightValue = 1f;
        private Float bottomValue = null;
        private Float leftValue = null;

        private Unit topUnits = Unit.PIXELS;
        private Unit rightUnits = Unit.PIXELS;
        private Unit bottomUnits = Unit.PIXELS;
        private Unit leftUnits = Unit.PIXELS;

        /**
         * Sets the position attributes using CSS syntax. Attributes not
         * included in the string are reset to their unset states.
         *
         * <code><pre>
         * setCSSString("top:10px;left:20%;z-index:16;");
         * </pre></code>
         *
         * @param css
         */
        public void setCSSString(String css) {
            topValue = rightValue = bottomValue = leftValue = null;
            topUnits = rightUnits = bottomUnits = leftUnits = Unit.PIXELS;
            zIndex = -1;
            if (css == null) {
                return;
            }

            for (String cssProperty : css.split(";")) {
                String[] keyValuePair = cssProperty.split(":");
                String key = keyValuePair[0].trim();
                if (key.isEmpty()) {
                    continue;
                }
                if (key.equals("z-index")) {
                    zIndex = Integer.parseInt(keyValuePair[1].trim());
                } else {
                    String value;
                    if (keyValuePair.length > 1) {
                        value = keyValuePair[1].trim();
                    } else {
                        value = "";
                    }
                    String symbol = value.replaceAll("[0-9\\.\\-]+", "");
                    if (!symbol.isEmpty()) {
                        value = value.substring(0, value.indexOf(symbol))
                                .trim();
                    }
                    if (value.isEmpty()) {
                        break;
                    }
                    float v = Float.parseFloat(value);
                    Unit unit = Unit.getUnitFromSymbol(symbol);
                    if (key.equals("top")) {
                        topValue = v;
                        topUnits = unit;
                    } else if (key.equals("right")) {
                        rightValue = v;
                        rightUnits = unit;
                    } else if (key.equals("bottom")) {
                        bottomValue = v;
                        bottomUnits = unit;
                    } else if (key.equals("left")) {
                        leftValue = v;
                        leftUnits = unit;
                    }
                }
            }
        }

        /**
         * Converts the internal values into a valid CSS string.
         *
         * @return A valid CSS string
         */
        public String getCSSString() {
            String s = "";
            if (topValue != null) {
                s += "top:" + topValue + topUnits.getSymbol() + ";";
            }
            if (rightValue != null) {
                s += "right:" + rightValue + rightUnits.getSymbol() + ";";
            }
            if (bottomValue != null) {
                s += "bottom:" + bottomValue + bottomUnits.getSymbol() + ";";
            }
            if (leftValue != null) {
                s += "left:" + leftValue + leftUnits.getSymbol() + ";";
            }
            if (zIndex >= 0) {
                s += "z-index:" + zIndex + ";";
            }
            return s;
        }

        /**
         * Sets the 'top' attribute; distance from the top of the component to
         * the top edge of the layout.
         *
         * @param topValue
         *            The value of the 'top' attribute
         * @param topUnits
         *            The unit of the 'top' attribute. See UNIT_SYMBOLS for a
         *            description of the available units.
         */
        public void setTop(Float topValue, Unit topUnits) {
            this.topValue = topValue;
            this.bottomValue = null;
            this.topUnits = topUnits;
            updateComponentPosition(this);
        }

        /**
         * Sets the 'right' attribute; distance from the right of the component
         * to the right edge of the layout.
         *
         * @param rightValue
         *            The value of the 'right' attribute
         * @param rightUnits
         *            The unit of the 'right' attribute. See UNIT_SYMBOLS for a
         *            description of the available units.
         */
        public void setRight(Float rightValue, Unit rightUnits) {
            this.rightValue = rightValue;
            this.leftValue = null;
            this.rightUnits = rightUnits;
            updateComponentPosition(this);
        }

        /**
         * Sets the 'bottom' attribute; distance from the bottom of the
         * component to the bottom edge of the layout.
         *
         * @param bottomValue
         *            The value of the 'bottom' attribute
         * @param bottomUnits
         *            The unit of the 'bottom' attribute. See UNIT_SYMBOLS for a
         *            description of the available units.
         */
        public void setBottom(Float bottomValue, Unit bottomUnits) {
            this.bottomValue = bottomValue;
            this.topValue = null;
            this.bottomUnits = bottomUnits;
            updateComponentPosition(this);
        }

        /**
         * Sets the 'left' attribute; distance from the left of the component to
         * the left edge of the layout.
         *
         * @param leftValue
         *            The value of the 'left' attribute
         * @param leftUnits
         *            The unit of the 'left' attribute. See UNIT_SYMBOLS for a
         *            description of the available units.
         */
        public void setLeft(Float leftValue, Unit leftUnits) {
            this.leftValue = leftValue;
            this.rightValue = null;
            this.leftUnits = leftUnits;
            updateComponentPosition(this);
        }

        /**
         * Sets the 'z-index' attribute; the visual stacking order.
         *
         * @param zIndex
         *            The z-index for the component.
         */
        public void setZIndex(int zIndex) {
            this.zIndex = zIndex;
            updateComponentPosition(this);
        }

        /**
         * Sets the value of the 'top' attribute; distance from the top of the
         * component to the top edge of the layout.
         *
         * @param topValue
         *            The value of the 'left' attribute
         */
        public void setTopValue(Float topValue) {
            this.topValue = topValue;
            this.bottomValue = null;
            updateComponentPosition(this);
        }

        /**
         * Gets the 'top' attributes value in current units.
         *
         * @see #getTopUnits()
         * @return The value of the 'top' attribute, null if not set
         */
        public Float getTopValue() {
            return topValue;
        }

        /**
         * Gets the 'right' attributes value in current units.
         *
         * @return The value of the 'right' attribute, null if not set
         * @see #getRightUnits()
         */
        public Float getRightValue() {
            return rightValue;
        }

        /**
         * Sets the 'right' attribute value (distance from the right of the
         * component to the right edge of the layout). Currently active units
         * are maintained.
         *
         * @param rightValue
         *            The value of the 'right' attribute
         * @see #setRightUnits(Unit)
         */
        public void setRightValue(Float rightValue) {
            this.rightValue = rightValue;
            this.leftValue = null;
            updateComponentPosition(this);
        }

        /**
         * Gets the 'bottom' attributes value using current units.
         *
         * @return The value of the 'bottom' attribute, null if not set
         * @see #getBottomUnits()
         */
        public Float getBottomValue() {
            return bottomValue;
        }

        /**
         * Sets the 'bottom' attribute value (distance from the bottom of the
         * component to the bottom edge of the layout). Currently active units
         * are maintained.
         *
         * @param bottomValue
         *            The value of the 'bottom' attribute
         * @see #setBottomUnits(Unit)
         */
        public void setBottomValue(Float bottomValue) {
            this.bottomValue = bottomValue;
            this.topValue = null;
            updateComponentPosition(this);
        }

        /**
         * Gets the 'left' attributes value using current units.
         *
         * @return The value of the 'left' attribute, null if not set
         * @see #getLeftUnits()
         */
        public Float getLeftValue() {
            return leftValue;
        }

        /**
         * Sets the 'left' attribute value (distance from the left of the
         * component to the left edge of the layout). Currently active units are
         * maintained.
         *
         * @param leftValue
         *            The value of the 'left' CSS-attribute
         * @see #setLeftUnits(Unit)
         */
        public void setLeftValue(Float leftValue) {
            this.leftValue = leftValue;
            this.rightValue = null;
            updateComponentPosition(this);
        }

        /**
         * Gets the unit for the 'top' attribute.
         *
         * @return See {@link Sizeable} UNIT_SYMBOLS for a description of the
         *         available units.
         */
        public Unit getTopUnits() {
            return topUnits;
        }

        /**
         * Sets the unit for the 'top' attribute.
         *
         * @param topUnits
         *            See {@link Sizeable} UNIT_SYMBOLS for a description of the
         *            available units.
         */
        public void setTopUnits(Unit topUnits) {
            this.topUnits = topUnits;
            updateComponentPosition(this);
        }

        /**
         * Gets the unit for the 'right' attribute.
         *
         * @return See {@link Sizeable} UNIT_SYMBOLS for a description of the
         *         available units.
         */
        public Unit getRightUnits() {
            return rightUnits;
        }

        /**
         * Sets the unit for the 'right' attribute.
         *
         * @param rightUnits
         *            See {@link Sizeable} UNIT_SYMBOLS for a description of the
         *            available units.
         */
        public void setRightUnits(Unit rightUnits) {
            this.rightUnits = rightUnits;
            updateComponentPosition(this);
        }

        /**
         * Gets the unit for the 'bottom' attribute.
         *
         * @return See {@link Sizeable} UNIT_SYMBOLS for a description of the
         *         available units.
         */
        public Unit getBottomUnits() {
            return bottomUnits;
        }

        /**
         * Sets the unit for the 'bottom' attribute.
         *
         * @param bottomUnits
         *            See {@link Sizeable} UNIT_SYMBOLS for a description of the
         *            available units.
         */
        public void setBottomUnits(Unit bottomUnits) {
            this.bottomUnits = bottomUnits;
            updateComponentPosition(this);
        }

        /**
         * Gets the unit for the 'left' attribute.
         *
         * @return See {@link Sizeable} UNIT_SYMBOLS for a description of the
         *         available units.
         */
        public Unit getLeftUnits() {
            return leftUnits;
        }

        /**
         * Sets the unit for the 'left' attribute.
         *
         * @param leftUnits
         *            See {@link Sizeable} UNIT_SYMBOLS for a description of the
         *            available units.
         */
        public void setLeftUnits(Unit leftUnits) {
            this.leftUnits = leftUnits;
            updateComponentPosition(this);
        }

        /**
         * Gets the 'z-index' attribute.
         *
         * @return the zIndex The z-index attribute
         */
        public int getZIndex() {
            return zIndex;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return getCSSString();
        }

    }

}
