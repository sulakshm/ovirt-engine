package org.ovirt.engine.ui.webadmin.section.main.view;

import org.ovirt.engine.ui.common.view.AbstractTabPanelView;
import org.ovirt.engine.ui.common.widget.tab.AbstractTabPanel;
import org.ovirt.engine.ui.common.widget.tab.MenuLayout;
import org.ovirt.engine.ui.common.widget.tab.TabWidgetHandler;
import org.ovirt.engine.ui.webadmin.section.main.presenter.MainTabPanelPresenter;
import org.ovirt.engine.ui.webadmin.widget.tab.HeadlessTabPanel;

import com.google.inject.Inject;

public class MainTabPanelView extends AbstractTabPanelView implements MainTabPanelPresenter.ViewDef {

    private final HeadlessTabPanel tabPanel;

    @Inject
    public MainTabPanelView(MenuLayout menuLayout) {
        tabPanel = new HeadlessTabPanel(menuLayout);
        initWidget(getTabPanel());
    }

    @Override
    protected Object getContentSlot() {
        return MainTabPanelPresenter.TYPE_SetTabContent;
    }

    @Override
    protected AbstractTabPanel getTabPanel() {
        return tabPanel;
    }

    @Override
    public void setTabWidgetHandler(TabWidgetHandler tabWidgetHandler) {
        tabPanel.setTabWidgetHandler(tabWidgetHandler);
    }
}
