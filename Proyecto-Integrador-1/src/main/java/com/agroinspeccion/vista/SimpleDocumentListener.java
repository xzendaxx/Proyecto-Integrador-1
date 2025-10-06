package com.agroinspeccion.vista;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

/**
 * Implementación sencilla de {@link DocumentListener} basada en una función lambda.
 */
@FunctionalInterface
public interface SimpleDocumentListener extends DocumentListener {

    void update(DocumentEvent e);

    @Override
    default void insertUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        update(e);
    }

    static SimpleDocumentListener of(Consumer<DocumentEvent> consumer) {
        return consumer::accept;
    }
}
