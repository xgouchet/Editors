package fr.xgouchet.xmleditor.core.editor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;

import fr.xgouchet.xmleditor.core.actions.ActionQueueExecutor;
import fr.xgouchet.xmleditor.core.actions.AsyncActionListener;
import fr.xgouchet.xmleditor.core.actions.LoadXmlAction;
import fr.xgouchet.xmleditor.core.actions.SaveXmlAction;
import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.model.XmlNodeFactory;
import fr.xgouchet.xmleditor.core.utils.InputStreamProvider;

/**
 * This class handles all manipulation performed on an XML document tree
 *
 * @author Xavier Gouchet
 */
public class XmlEditor {


    private final ActionQueueExecutor mActionQueueExecutor = new ActionQueueExecutor();
    private XmlNode mDocument;


    /**
     * @return the current document held by this editor
     */
    @NonNull
    public XmlNode getDocument() {
        return mDocument;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // EDITOR INPUT/OUTPUT
    //////////////////////////////////////////////////////////////////////////////////////


    /**
     * Asynchronously loads an XML document from the given input stream provider, and replaces the
     * current document
     *
     * @param inputProvider a provider to the input stream representing the document to load
     */
    public void loadDocument(final @NonNull InputStreamProvider<?> inputProvider) {
        LoadXmlAction loaderAction = new LoadXmlAction();
        AsyncActionListener<XmlNode> listener = new AsyncActionListener<XmlNode>() {
            @Override
            public void onActionPerformed(@Nullable XmlNode output) {
                if (output == null) {
                    // TODO notify listener that an error occurred
                } else {
                    mDocument = output;
                    // TODO notify listener that the doc changed
                }
            }

            @Override
            public void onActionFailed(Exception e) {
                // TODO notify listener that an error occurred
            }
        };

        mActionQueueExecutor.queueAction(loaderAction, inputProvider, listener);
    }


    /**
     * Immediately clears the current document and replace it with the standard empty doc.
     */
    public void clearDocument() {
        XmlNode doc = XmlNodeFactory.createDocument();
        XmlNodeFactory.createDocumentDeclaration(doc);
        mDocument = doc;
        // TODO notify listener that the doc has changed
    }

    /**
     * Asynchronously saves this document to the given output stream, as a standard, pretty printed
     * XML document.
     * <p/>
     * As long as this operation is running, the document is set to read only.
     *
     * @param output the output stream to write into
     */
    public void saveDocument(final @NonNull ByteArrayOutputStream output) {
        SaveXmlAction saveAction = new SaveXmlAction();

        SaveXmlAction.Input input = new SaveXmlAction.Input(mDocument, output);

        // TODO block all operations while this is performing
        AsyncActionListener<Void> listener = new AsyncActionListener<Void>() {
            @Override
            public void onActionPerformed(@Nullable Void output) {
                // TODO notify listener that the doc has been saved
            }

            @Override
            public void onActionFailed(Exception e) {
                // TODO notify listener that an error occurred
            }
        };

        mActionQueueExecutor.queueAction(saveAction, input, listener);
    }
}
