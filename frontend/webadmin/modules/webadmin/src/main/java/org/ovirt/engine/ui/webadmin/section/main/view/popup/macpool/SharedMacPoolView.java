package org.ovirt.engine.ui.webadmin.section.main.view.popup.macpool;

import java.util.List;

import org.gwtbootstrap3.client.ui.Container;
import org.ovirt.engine.core.common.businessentities.MacPool;
import org.ovirt.engine.ui.common.MainTableHeaderlessResources;
import org.ovirt.engine.ui.common.MainTableResources;
import org.ovirt.engine.ui.common.system.ClientStorage;
import org.ovirt.engine.ui.common.widget.table.SimpleActionTable;
import org.ovirt.engine.ui.common.widget.table.column.AbstractImageResourceColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractTextColumn;
import org.ovirt.engine.ui.common.widget.uicommon.permissions.PermissionListModelTable;
import org.ovirt.engine.ui.uicommonweb.models.configure.PermissionListModel;
import org.ovirt.engine.ui.uicommonweb.models.macpool.SharedMacPoolListModel;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.ApplicationResources;
import org.ovirt.engine.ui.webadmin.gin.AssetProvider;
import org.ovirt.engine.ui.webadmin.uicommon.model.PermissionModelProvider;
import org.ovirt.engine.ui.webadmin.uicommon.model.SharedMacPoolModelProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.inject.Inject;

public class SharedMacPoolView extends Composite {

    interface ViewUiBinder extends UiBinder<Container, SharedMacPoolView> {
        ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
    }

    @UiField
    SplitLayoutPanel splitLayoutPanel;

    @UiField
    FlowPanel macPoolPanel;

    @UiField
    FlowPanel authorizationPanel;

    private final PermissionModelProvider<MacPool, SharedMacPoolListModel> permissionModelProvider;
    private final SharedMacPoolModelProvider sharedMacPoolModelProvider;

    private final EventBus eventBus;
    private final ClientStorage clientStorage;

    private static final ApplicationResources resources = AssetProvider.getResources();
    private static final ApplicationConstants constants = AssetProvider.getConstants();

    @Inject
    public SharedMacPoolView(final EventBus eventBus, final ClientStorage clientStorage,
            SharedMacPoolModelProvider sharedMacPoolModelProvider,
            PermissionModelProvider<MacPool, SharedMacPoolListModel> permissionModelProvider,
            SharedMacPoolActionPanelPresenterWidget actionPanel) {
        this.eventBus = eventBus;
        this.clientStorage = clientStorage;
        this.sharedMacPoolModelProvider = sharedMacPoolModelProvider;
        this.permissionModelProvider = permissionModelProvider;

        initWidget(ViewUiBinder.uiBinder.createAndBindUi(this));

        macPoolPanel.add(actionPanel);
        macPoolPanel.add(createMacPoolTable());

        PermissionListModelTable<PermissionListModel<MacPool>> authorizationTable = new PermissionListModelTable<>(permissionModelProvider, eventBus, null, clientStorage);
        authorizationTable.initTable();
        authorizationTable.getTable().getSelectionModel().addSelectionChangeHandler(event ->
                permissionModelProvider.setSelectedItems(authorizationTable.getTable().getSelectedItems()));
        authorizationPanel.add(authorizationTable);

        setupAuthorizationPanelVisibility(false);
    }

    private void setupAuthorizationPanelVisibility(boolean visible) {
        splitLayoutPanel.clear();
        if (visible) {
            splitLayoutPanel.addSouth(authorizationPanel, 150);
        }
        splitLayoutPanel.add(macPoolPanel);
    }

    private SimpleActionTable<MacPool> createMacPoolTable() {
        SimpleActionTable<MacPool> macPoolTable =
                new SimpleActionTable<>(sharedMacPoolModelProvider,
                        getTableHeaderlessResources(),
                        getTableResources(),
                        eventBus,
                        clientStorage);

        macPoolTable.addColumn(new AbstractImageResourceColumn<MacPool>() {
            @Override
            public ImageResource getValue(MacPool macPool) {
                return macPool.isDefaultPool() ? resources.lockImage() : null;
            }
        }, constants.empty(), "20px"); //$NON-NLS-1$

        macPoolTable.addColumn(new AbstractTextColumn<MacPool>() {
            @Override
            public String getValue(MacPool macPool) {
                return macPool.getName();
            }
        }, constants.configureMacPoolNameColumn(), "100px"); //$NON-NLS-1$

        macPoolTable.addColumn(new AbstractTextColumn<MacPool>() {
            @Override
            public String getValue(MacPool macPool) {
                return macPool.getDescription();
            }
        }, constants.configureMacPoolDescriptionColumn(), "300px"); //$NON-NLS-1$

        macPoolTable.getSelectionModel().addSelectionChangeHandler(event -> {
            final List<MacPool> selectedItems = macPoolTable.getSelectedItems();
            sharedMacPoolModelProvider.setSelectedItems(selectedItems);

            final PermissionListModel<MacPool> model = permissionModelProvider.getModel();

            if (selectedItems.size() == 1) {
                model.setEntity(selectedItems.get(0));
                setupAuthorizationPanelVisibility(true);
            } else {
                model.setEntity(null);
                setupAuthorizationPanelVisibility(false);
            }
        });

        return macPoolTable;
    }

    protected Resources getTableHeaderlessResources() {
        return (Resources) GWT.create(MainTableHeaderlessResources.class);
    }

    protected Resources getTableResources() {
        return (Resources) GWT.create(MainTableResources.class);
    }

}
