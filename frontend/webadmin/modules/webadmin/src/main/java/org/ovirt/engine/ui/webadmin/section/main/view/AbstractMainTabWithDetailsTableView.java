package org.ovirt.engine.ui.webadmin.section.main.view;

import java.util.List;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.ListItem;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.client.ui.html.UnorderedList;
import org.ovirt.engine.ui.common.css.PatternflyConstants;
import org.ovirt.engine.ui.common.presenter.ActionPanelPresenterWidget;
import org.ovirt.engine.ui.common.uicommon.model.MainModelProvider;
import org.ovirt.engine.ui.common.widget.PatternflyIconType;
import org.ovirt.engine.ui.common.widget.table.SimpleActionTable;
import org.ovirt.engine.ui.uicommonweb.models.ListWithDetailsModel;
import org.ovirt.engine.ui.uicommonweb.models.tags.TagModel;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.gin.AssetProvider;
import org.ovirt.engine.ui.webadmin.section.main.presenter.AbstractMainTabWithDetailsPresenter;
import org.ovirt.engine.ui.webadmin.section.main.presenter.DetailsTransitionHandler;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.gwtplatform.mvp.client.PresenterWidget;

/**
 * Base class for table-based main tab views that work with {@link ListWithDetailsModel}.
 *
 * @param <T>
 *            Table row data type.
 * @param <M>
 *            Main model type.
 */
public abstract class AbstractMainTabWithDetailsTableView<T, M extends ListWithDetailsModel> extends AbstractMainTabTableView<T, M>
        implements AbstractMainTabWithDetailsPresenter.ViewDef<T> {

    private static final String OBRAND_MAIN_TAB = "obrand_main_tab"; // $NON-NLS-1$

    protected DetailsTransitionHandler<T> transitionHandler;

    private Column breadCrumbsColumn;

    private final FlowPanel actionSearchPanel = new FlowPanel();

    private Row resultRow;
    private UnorderedList resultList;

    private IsWidget searchPanel;
    private ActionPanelPresenterWidget<T, M> actionPanel;

    private static final ApplicationConstants constants = AssetProvider.getConstants();

    public AbstractMainTabWithDetailsTableView(MainModelProvider<T, M> modelProvider) {
        super(modelProvider);
        SimpleActionTable<T> table = getTable();
        FlowPanel tableContainer = table.getOuterWidget();
        addBreadCrumbs(tableContainer);
        addActionSearchPanel(tableContainer);
        table.addStyleName(OBRAND_MAIN_TAB);
        table.addStyleName(Styles.CONTAINER_FLUID);
    }

    @Override
    public void setDetailPlaceTransitionHandler(DetailsTransitionHandler<T> handler) {
        this.transitionHandler = handler;
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == AbstractMainTabWithDetailsPresenter.TYPE_SetSearchPanel) {
            if (content != null) {
                if (actionPanel == null) {
                    searchPanel = content;
                } else {
                    actionPanel.setSearchPanel((PresenterWidget<?>) content);
                }
            } else {
                searchPanel = null;
                if (actionPanel != null) {
                    actionPanel.setSearchPanel(null);
                }
            }
        } else if (slot == AbstractMainTabWithDetailsPresenter.TYPE_SetActionPanel) {
            if (content != null) {
                actionSearchPanel.add(content);
                this.actionPanel = (ActionPanelPresenterWidget<T, M>) content;
                if (searchPanel != null) {
                    actionPanel.setSearchPanel((PresenterWidget<?>) searchPanel);
                }
                addResultPanel(actionPanel);
            } else {
                actionSearchPanel.clear();
                this.actionPanel = null;
                resultRow.clear();
                resultRow = null;
            }
        } else if (slot == AbstractMainTabWithDetailsPresenter.TYPE_SetBreadCrumbs) {
            breadCrumbsColumn.clear();
            if (content != null) {
                breadCrumbsColumn.add(content);
            }
        } else {
            super.setInSlot(slot, content);
        }
    }

    @Override
    public IsWidget getTableContainer() {
        return super.getTable().getOuterWidget();
    }

    private void addBreadCrumbs(FlowPanel container) {
        Row breadCrumbsRow = new Row();
        breadCrumbsColumn = new Column(ColumnSize.SM_12);
        breadCrumbsRow.add(breadCrumbsColumn);
        container.insert(breadCrumbsRow, 0);
    }

    private void addActionSearchPanel(FlowPanel container) {
        container.insert(actionSearchPanel, 1);
    }

    private void addResultPanel(ActionPanelPresenterWidget<T, M> actionPanel) {
        resultRow = new Row();
        resultRow.addStyleName(PatternflyConstants.PF_TOOLBAR_RESULTS);

        FlowPanel resultColumn = new FlowPanel();
        resultRow.add(resultColumn);

        resultColumn.add(new Paragraph(constants.activeTags() + ":")); // $NON-NLS-1$
        resultList = new UnorderedList();
        resultList.addStyleName(Styles.LIST_INLINE);
        resultList.getElement().getStyle().setPaddingLeft(10, Unit.PX);
        resultColumn.add(resultList);
        resultRow.setVisible(false);
        actionPanel.setFilterResultPanel(resultRow);
    }

    public void setActiveTags(List<TagModel> tags) {
        resultList.clear();
        for (final TagModel tag: tags) {
            ListItem tagItem = new ListItem();
            Span label = new Span();
            label.addStyleName(Styles.LABEL);
            label.addStyleName(PatternflyConstants.PF_LABEL_INFO);
            label.setText(tag.getName().getEntity());
            Anchor deactivateAnchor = new Anchor();
            Span closeIconSpan = new Span();
            closeIconSpan.addStyleName(PatternflyIconType.PF_BASE.getCssName());
            closeIconSpan.addStyleName(PatternflyIconType.PF_CLOSE.getCssName());
            deactivateAnchor.add(closeIconSpan);
            deactivateAnchor.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    tag.setSelection(false);
                }

            });
            label.add(deactivateAnchor);
            tagItem.add(label);
            resultList.add(tagItem);
        }
        resultRow.setVisible(!tags.isEmpty());
    }
}
