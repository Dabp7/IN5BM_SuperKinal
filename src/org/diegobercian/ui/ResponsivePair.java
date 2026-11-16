package org.diegobercian.ui;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/** Displays two regions side by side on wide screens and stacks them on narrow screens. */
public final class ResponsivePair extends Pane {
    private double breakpoint = 720;
    private double firstWidth;
    private double gap;
    public ResponsivePair() { setMinWidth(0); }
    public double getBreakpoint() { return breakpoint; }
    public void setBreakpoint(double value) { breakpoint = value; requestLayout(); }
    public double getFirstWidth() { return firstWidth; }
    public void setFirstWidth(double value) { firstWidth = value; requestLayout(); }
    public double getGap() { return gap; }
    public void setGap(double value) { gap = value; requestLayout(); }
    @Override public Orientation getContentBias() { return Orientation.HORIZONTAL; }
    @Override protected double computeMinWidth(double height) { return 0; }
    @Override protected double computePrefWidth(double height) { return Math.max(930, breakpoint); }
    @Override protected double computeMinHeight(double width) { return computePrefHeight(width); }
    private double left(double width) { return Math.min(width - gap, firstWidth > 0 ? firstWidth : (width - gap) / 2); }
    @Override protected double computePrefHeight(double width) {
        if (getManagedChildren().size() != 2) return super.computePrefHeight(width);
        if (width < 0) width = computePrefWidth(-1);
        Node first = getManagedChildren().get(0), second = getManagedChildren().get(1);
        if (width < breakpoint) return first.prefHeight(width) + gap + second.prefHeight(width);
        double left = left(width);
        return Math.max(first.prefHeight(left), second.prefHeight(width - gap - left));
    }
    @Override protected void layoutChildren() {
        if (getManagedChildren().size() != 2) { super.layoutChildren(); return; }
        Node first = getManagedChildren().get(0), second = getManagedChildren().get(1);
        double width = getWidth();
        if (width < breakpoint) {
            double height = first.prefHeight(width);
            first.resizeRelocate(0, 0, width, height);
            second.resizeRelocate(0, height + gap, width, Math.max(second.prefHeight(width), getHeight() - height - gap));
        } else {
            double left = left(width);
            first.resizeRelocate(0, 0, left, getHeight());
            second.resizeRelocate(left + gap, 0, width - left - gap, getHeight());
        }
    }
}
